package com.vaiak.moto_compare.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.vaiak.moto_compare.exceptions.UserNotFoundException;
import com.vaiak.moto_compare.security.dto.AuthResponse;
import com.vaiak.moto_compare.security.dto.GoogleLoginRequest;
import com.vaiak.moto_compare.security.dto.LoginRequest;
import com.vaiak.moto_compare.security.dto.RegisterRequest;
import com.vaiak.moto_compare.security.jwt.JwtTokenProvider;
import com.vaiak.moto_compare.security.models.RefreshToken;
import com.vaiak.moto_compare.security.models.User;
import com.vaiak.moto_compare.security.refreshToken.RefreshTokenService;
import com.vaiak.moto_compare.security.services.GoogleOAuthService;
import com.vaiak.moto_compare.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${jwt.access-token-duration-millis}")
    private long accessTokenDurationMillis;
    @Value("${jwt.refresh-token-duration-days}")
    private long refreshTokenDurationDays;

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GoogleOAuthService googleOAuthService;

    public AuthController(UserService userService,
                          JwtTokenProvider jwtTokenProvider,
                          RefreshTokenService refreshTokenService,
                          BCryptPasswordEncoder passwordEncoder,
                          GoogleOAuthService googleOAuthService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.googleOAuthService = googleOAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletResponse response) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail());

            // Check if this is a Google user trying to login with email/password
            if (user.getIsGoogleUser() != null && user.getIsGoogleUser()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("This account is linked to Google. Please use Google Sign-In.");
            }

            if (user == null || user.getPassword() == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }

            String accessToken = jwtTokenProvider.generateToken(user.getEmail(), user.getRole(), accessTokenDurationMillis);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            addRefreshTokenCookie(response, refreshToken.getToken());
            return ResponseEntity.ok(new AuthResponse(accessToken));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    @Transactional
    public ResponseEntity<?> logout(Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        refreshTokenService.deleteByUser(user);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest,
                                      HttpServletResponse response) {
        if (userService.findByEmailOptional(registerRequest.getEmail()).isPresent()) {
           throw new RuntimeException("Email already exists");
        }
        if (userService.findByUserNameOptional(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUserName(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        userService.saveUser(user);

        String accessToken = jwtTokenProvider.generateToken(user.getEmail(), user.getRole(), accessTokenDurationMillis);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        addRefreshTokenCookie(response, refreshToken.getToken());

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }


    private void addRefreshTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(Duration.ofDays(refreshTokenDurationDays))
                .sameSite("Strict")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String tokenFromCookie = extractRefreshTokenFromCookie(request);

        RefreshToken refreshToken = refreshTokenService.findByToken(tokenFromCookie);
        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();
        String newAccessToken = jwtTokenProvider.generateToken(user.getEmail(), user.getRole(), accessTokenDurationMillis);

        return ResponseEntity.ok(new AuthResponse(newAccessToken));
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new RuntimeException("No refresh token found in cookies");
    }

    @PutMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<String> resetPassword(@RequestParam String newPassword,
                                                //TODO ask for old password.
                                                Authentication auth) {
        //TODO use verified email to reset the password, in case user forgets it.
        String encrypted = passwordEncoder.encode(newPassword);
        String email = auth.getName();
        User user = userService.findByEmail(email);
        user.setPassword(encrypted);
        user.setUpdatedAt(LocalDateTime.now());
        userService.saveUser(user);

        return ResponseEntity.ok("Password has been reset");
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody @Valid GoogleLoginRequest googleLoginRequest,
                                         HttpServletResponse response) {
        try {
            // Verify the Google ID token
            GoogleIdToken.Payload payload = googleOAuthService.verifyToken(googleLoginRequest.getIdToken());

            String email = googleOAuthService.extractEmail(payload);
            String googleId = payload.getSubject(); // Google user ID
            String name = googleOAuthService.extractName(payload);
            String profilePictureUrl = googleOAuthService.extractPicture(payload);

            // Check if user already exists by email or Google ID
            User user = userService.findByEmailOptional(email)
                    .orElse(userService.findByGoogleId(googleId).orElse(null));

            if (user == null) {
                // Create new Google user
                user = userService.createGoogleUser(email, googleId, name, profilePictureUrl);
            } else if (!user.getIsGoogleUser()) {
                // Existing email user trying to login with Google
                // Link the Google account to existing user
                user.setGoogleId(googleId);
                user.setIsGoogleUser(true);
                user.setProfilePictureUrl(profilePictureUrl);
                userService.saveUser(user);
            }

            // Generate tokens and return response
            String accessToken = jwtTokenProvider.generateToken(user.getEmail(), user.getRole(), accessTokenDurationMillis);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            addRefreshTokenCookie(response, refreshToken.getToken());

            return ResponseEntity.ok(new AuthResponse(accessToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Google authentication failed: " + e.getMessage());
        }
    }
}

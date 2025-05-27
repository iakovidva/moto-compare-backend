package com.vaiak.moto_compare.security;

import com.vaiak.moto_compare.security.dto.AuthResponse;
import com.vaiak.moto_compare.security.dto.LoginRequest;
import com.vaiak.moto_compare.security.dto.RegisterRequest;
import com.vaiak.moto_compare.security.jwt.JwtTokenProvider;
import com.vaiak.moto_compare.security.models.RefreshToken;
import com.vaiak.moto_compare.security.models.User;
import com.vaiak.moto_compare.security.refreshToken.RefreshTokenService;
import com.vaiak.moto_compare.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public AuthController(UserService userService,
                          JwtTokenProvider jwtTokenProvider,
                          RefreshTokenService refreshTokenService,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletResponse response) {
        User user = userService.findByEmail(loginRequest.getEmail());

        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        String accessToken = jwtTokenProvider.generateToken(user.getEmail(), user.getRole(), accessTokenDurationMillis);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        addRefreshTokenCookie(response, refreshToken.getToken());
        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<?> logout(Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        refreshTokenService.deleteByUser(user);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest,
                                      HttpServletResponse response) {
        if (userService.findByEmail(registerRequest.getEmail()) != null) {
           throw new RuntimeException("Email already exists");  //TODO USE MORE DESCRIPTIVE EXCEPTION - Check ExceptionHandler
        }
        if (userService.findByUserName(registerRequest.getUsername()) != null) {
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
}

package com.vaiak.moto_compare.security.jwt;

import static org.junit.jupiter.api.Assertions.*;

import com.vaiak.moto_compare.security.Role;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

  private final JwtTokenProvider jwtTokenProvider =
      new JwtTokenProvider("z7k4e9p2Yv6q1x3W8c0r5T2b7U1w9L4m6N0o3P5i8K9j2H7g4F5d3S6a8J1h3G5");

    @Test
    void test1() {
        String token = jwtTokenProvider.generateToken("bob", Role.USER, 86400000);
        System.out.println("created token: " + token);

        String role = jwtTokenProvider.getRoleFromToken(token);
        System.out.println("decrypted token: " + role);

        String emailFromToken = jwtTokenProvider.getEmailFromToken(token);
        System.out.println("user: " + emailFromToken);

        Date expirationDateFromToken = jwtTokenProvider.getExpirationDateFromToken(token);
    System.out.println(expirationDateFromToken);
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        String token = jwtTokenProvider.generateToken("testUser", Role.USER, 86400000);

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForExpiredToken() {
        // Arrange - create token with immediate expiration
        JwtTokenProvider expiredProvider = 
                new JwtTokenProvider("z7k4e9p2Yv6q1x3W8c0r5T2b7U1w9L4m6N0o3P5i8K9j2H7g4F5d3S6a8J1h3G5");
        String expiredToken = expiredProvider.generateToken("testUser", Role.USER, 0);

        // Wait a bit to ensure expiration
        try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException ignored) {}

        // Act
        boolean isValid = jwtTokenProvider.validateToken(expiredToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void getUsernameFromToken_ShouldReturnCorrectEmail() {
        // Arrange
        String expectedUsername = "testUser";
        String token = jwtTokenProvider.generateToken(expectedUsername, Role.USER, 86400000);

        // Act
        String actualEmail = jwtTokenProvider.getEmailFromToken(token);

        // Assert
        assertEquals(expectedUsername, actualEmail);
    }

    @Test
    void getRoleFromToken_ShouldReturnCorrectRole() {
        // Arrange
        String token = jwtTokenProvider.generateToken("testUser", Role.ADMIN, 86400000);

        // Act
        String actualRole = jwtTokenProvider.getRoleFromToken(token);

        // Assert
        assertEquals(Role.ADMIN.name(), actualRole);
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.string";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForTamperedToken() {
        // Arrange
        String originalToken = jwtTokenProvider.generateToken("testUser", Role.USER, 86400000);
        String[] parts = originalToken.split("\\.");
        String tamperedToken = parts[0] + "." + parts[1] + ".tamperedSignature";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(tamperedToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForTokenWithDifferentKey() {
        // Arrange
        String token = jwtTokenProvider.generateToken("testUser", Role.USER, 86400000);

        // Create provider with different key
        JwtTokenProvider otherProvider =
                new JwtTokenProvider("differentSecretKeyDifferentSecretKeyDifferentSecretKeyDifferent");

        // Act
        boolean isValid = otherProvider.validateToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForNullToken() {
        assertFalse(jwtTokenProvider.validateToken(null));
    }

    @Test
    void validateToken_ShouldReturnFalseForEmptyToken() {
        assertFalse(jwtTokenProvider.validateToken(""));
    }

    @Test
    void getExpirationDateFromToken_ShouldReturnFutureDate() {
        // Arrange
        String token = jwtTokenProvider.generateToken("testUser", Role.USER, 86400000);

        // Act
        Date expiration = jwtTokenProvider.getExpirationDateFromToken(token);

        // Assert
        assertTrue(expiration.after(new Date()));
    }
}


package com.vaiak.moto_compare.security.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleOAuthService {

    @Value("${google.client-id}")
    private String googleClientId;

    private GoogleIdTokenVerifier verifier;

    private GoogleIdTokenVerifier getVerifier() {
        if (verifier == null) {
            verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();
        }
        return verifier;
    }

    public GoogleIdToken verifyIdToken(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdToken googleIdToken = getVerifier().verify(idToken);
        if (googleIdToken != null) {
            return googleIdToken;
        }
        throw new SecurityException("Invalid Google ID token");
    }

    public GoogleIdToken.Payload verifyToken(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdToken googleIdToken = verifyIdToken(idToken);
        return googleIdToken.getPayload();
    }

    public String extractEmail(GoogleIdToken.Payload payload) {
        return payload.getEmail();
    }

    public String extractName(GoogleIdToken.Payload payload) {
        return (String) payload.get("name");
    }

    public String extractGivenName(GoogleIdToken.Payload payload) {
        return (String) payload.get("given_name");
    }

    public String extractFamilyName(GoogleIdToken.Payload payload) {
        return (String) payload.get("family_name");
    }

    public String extractPicture(GoogleIdToken.Payload payload) {
        return (String) payload.get("picture");
    }

    public Boolean isEmailVerified(GoogleIdToken.Payload payload) {
        return payload.getEmailVerified();
    }
}

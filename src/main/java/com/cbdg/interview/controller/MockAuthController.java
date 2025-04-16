package com.cbdg.interview.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.time.Instant;

/*This class:

Generates RSA key pairs for signing and verifying JWTs
Defines mock users with different roles
Provides endpoints for token generation (/token) and key publication (/.well-known/jwks.json)
Creates JWTs with role claims that Spring Security can use for authorization*/
@RestController
public class MockAuthController {

    private final RSAKey rsaKey;
    private final JWSSigner signer;
    private final JWKSet jwkSet;

    // Mock users with their roles
    private final Map<String, List<String>> userRoles = new HashMap<>();

    public MockAuthController() throws JOSEException {
        // Generate RSA key pair for signing JWTs
        this.rsaKey = new RSAKeyGenerator(2048)
                .keyID(UUID.randomUUID().toString())
                .generate();
        this.signer = new RSASSASigner(rsaKey);
        this.jwkSet = new JWKSet(rsaKey.toPublicJWK());

        // Initialize mock users
        userRoles.put("user1", Arrays.asList("new_app_role", "user"));
        userRoles.put("admin", Arrays.asList("new_app_role", "admin"));
        userRoles.put("guest", Collections.singletonList("user"));

        System.out.println("MockAuthController initialized with users: " + userRoles.keySet());
    }

    @GetMapping("/token")
    public ResponseEntity<Map<String, Object>> getToken(
            @RequestParam(defaultValue = "user1") String username,
            @RequestParam(defaultValue = "password") String password) throws JOSEException {

        System.out.println("Token request received for user: " + username);

        // For testing, just generate a token regardless of credentials
        String token = generateToken(username);

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", token);
        response.put("token_type", "Bearer");
        response.put("expires_in", 3600);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/.well-known/jwks.json")
    public ResponseEntity<Map<String, Object>> jwks() {
        return ResponseEntity.ok(jwkSet.toJSONObject());
    }

    private String generateToken(String username) throws JOSEException {
        List<String> roles = userRoles.getOrDefault(username, Collections.singletonList("user"));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("http://localhost:8080")
                .audience("banking-app")
                .expirationTime(Date.from(Instant.now().plusSeconds(3600))) // 1 hour
                .issueTime(new Date())
                .claim("roles", roles)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(),
                claimsSet
        );

        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
}
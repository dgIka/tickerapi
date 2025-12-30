package org.dgika.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.dgika.security.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long ttlSeconds;

    public JwtService(
            @Value("${token.signing.key}") String base64Key,
            @Value("${token.ttl-seconds:86400}") long ttlSeconds
    ) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Key));
        this.ttlSeconds = ttlSeconds;
    }

    public String generateToken(UserDetailsImpl principal) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttlSeconds);

        return Jwts.builder()
                .subject(principal.getUsername())
                .claims(Map.of("uid", principal.getUserId().toString()))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public String extractSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public UUID extractUserId(String token) {
        String uid = parseClaims(token).get("uid", String.class);
        return UUID.fromString(uid);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims c = parseClaims(token);
            Date exp = c.getExpiration();
            return exp != null && exp.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

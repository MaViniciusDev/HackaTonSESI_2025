package org.utopia.hackatonsesi_2025.users.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.issuer:utopia-hackatonsesi}")
    private String issuer;

    @Value("${security.jwt.expires-in:3600}")
    private long expiresInSeconds;

    public String generateToken(UserDetails user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiresInSeconds);
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getUsername())
                .withArrayClaim("roles", roles.toArray(String[]::new))
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);
    }

    public DecodedJWT verify(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        return verifier.verify(token);
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }
}


package com.anae.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.*;
import com.anae.api.Enums.UserRole;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
    }

    public String generateJwt(Authentication auth) {
        return generateJwt(auth.getName(), auth.getAuthorities());
    }

    public String generateJwt(String username, Collection< ? extends GrantedAuthority> authorities){
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(username)
                .claim("roles",
                        authorities.stream().map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(" "))
                        )
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Map<String, Object> getClaims(String token) {
        return decodeJwtToken(token).getClaims();
    }

    private Jwt decodeJwtToken(String token) {
        return jwtDecoder.decode(token);
    }

    public boolean tokenContainsRole(String authorizationHeader, UserRole role) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Extract the token after "Bearer "
            // Your logic with the token here

            var claims = getClaims(token);

            var roles = claims.get("roles").toString();

            return roles.contains(role.name());
        } else {
            System.out.println("Invalid or missing Authorization header");
            return false;
        }
    }
}

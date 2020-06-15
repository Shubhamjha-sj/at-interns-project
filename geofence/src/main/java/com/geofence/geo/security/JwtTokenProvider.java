package com.geofence.geo.security;

import com.geofence.geo.model.Tenant;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.geofence.geo.security.SecurityConstants.EXPIRATION_TIME;
import static com.geofence.geo.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {
    public static final Logger LOGGER = LoggerFactory.getLogger("JwtTokenProvider Logger");

    public String generateToken(Authentication authentication) {
        try {
            Tenant tenant = (Tenant) authentication.getPrincipal();
            Date now = new Date(System.currentTimeMillis());

            Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

            String userId = tenant.getId();

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", (tenant.getId()));
            claims.put("name", tenant.getName());
            return Jwts.builder()
                    .setSubject(userId)
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .compact();

        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            LOGGER.debug("Invalid JWT Signature");

        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT Token");
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");

        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty");

        }
        return false;
    }


    //Get user Id from token

    public String getTenantIdFromJWT(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();


            return (String) claims.get("id");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getTenantIdFromJWTHeader(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.substring(7)).getBody();

            return (String) claims.get("id");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}

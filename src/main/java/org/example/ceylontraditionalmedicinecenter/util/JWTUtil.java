package org.example.ceylontraditionalmedicinecenter.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.ceylontraditionalmedicinecenter.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@PropertySource(ignoreResourceNotFound = true, value = "classpath:otherprops.properties")
public class JWTUtil implements Serializable {
    private static final long serialVersionUID = 234234523523L;

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 12;

    @Value("${jwt.secret}")

    private String secretKey;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Claims getUserRoleCodeFromToken(String token) {
        return getAllClaimsFromToken(token);
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object roleClaim = claims.get("roles");
        if (roleClaim instanceof List) {
            //noinspection unchecked
            return ((List<Object>) roleClaim).stream().map(Object::toString).collect(Collectors.toList());
        } else if (roleClaim instanceof String) {
            return List.of(roleClaim.toString());
        }

        String singleRole = claims.get("role", String.class);
        if (singleRole != null) {
            return List.of(singleRole);
        }

        return List.of();
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    //generate token for user
    public String generateToken(UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        String role = userDTO.getRole() == null ? "ROLE_USER" : userDTO.getRole().trim().toUpperCase();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        claims.put("roles", List.of(role));
        claims.put("role", role);
        return doGenerateToken(claims, userDTO.getEmail());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }


    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

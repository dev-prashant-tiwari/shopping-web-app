package com.inventoryService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

    public static final long jwtValidity = 3*60*60; //for 3 hours
    private String secretKey = "sgdhjfkdWRERCVasddsgerwbrvcxucnsfdWZVCGRGFVTGBSEDCbnmxceuweiQSDFVBHUJMzsderfcvghyuZXCV";

    public String getUserNameFromToken(String token){
        return getClaimFromToken(token,Claims::getSubject);
    }
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token,Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token){
        final Date expDate = getExpirationDateFromToken(token);
        return expDate.before(new Date());
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        String username = userDetails.getUsername();
        return Jwts.builder().setClaims(claims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()*jwtValidity*1000))
                .signWith(SignatureAlgorithm.HS512,secretKey).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        String username = getUserNameFromToken(token);
        return (!isTokenExpired(token) && username.equals(userDetails.getUsername()));
    }
}

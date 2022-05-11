package com.usta.jwt.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtility implements Serializable {
    private static final long serialVersionUID = 234234523523L;

    private static final long JWT_TOKEN_VALIDITY = 2 * 60 * 60;

    @Value("${jwt.secret}")
    private String secretKey;

    //Traer username del jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirantionDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);

    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //Metodo para traer cualquier informacion del token que necesite una llave secreta(Secret key)
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
    //Verificacion si el token ha expirado

    private Boolean isTokenExpired(String token){
        final Date expiration=getExpirantionDateFromToken(token);
        return  expiration.before(new Date());
    }
    //Generar el token para el usuario
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();
        return  doGenerateToken(claims,userDetails.getUsername());
    }
    //Generacion de Token
    public String doGenerateToken(Map<String,Object> claims, String subject){
        return  Jwts.builder().setClaims(claims).setSubject(subject).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000)).
                signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
    //Validar Items
    public boolean vvalidateToken(String token, UserDetails userDetails){
        final  String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}


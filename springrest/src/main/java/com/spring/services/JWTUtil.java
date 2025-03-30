package com.spring.services;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtil {
	 private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);

	 public static String secret = "MyJWTSecret";
	 
	 private String token;
	 private Claims claims;
	 private boolean tokenValid = false;
	 
	 public boolean isTokenValid() {
		return tokenValid;
	 }
	 
	 public String getSubject(){
		 return claims.getSubject();
	 }
	 
	 public Date getExiryDate(){
		 return claims.getExpiration();
	 }
	public JWTUtil(String token){
		 this.token = token;
		 try {
			 claims = extractClaims(token);
			 tokenValid = true;
          } catch (JwtException e) {
        	  log.error("JwtException : " , e);
          }
		 
	 }
	 
	 public  Object getClaimAttribute(String key){
		 return  claims.getOrDefault(key, "");
	 }
	 
	 public static Claims extractClaims(String token){
		 return  Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		
	 }
	 
	 public static String generateToken(Map<String, Object> claims, String subject){
		 return Jwts.builder().setClaims(claims).setIssuer("Apache-jjwt").setSubject(subject)
				 //.setExpiration(new Date(System.currentTimeMillis() + (1000* 60 * 60) ))
				 .signWith(SignatureAlgorithm.HS256, secret).compact();
	 }
}

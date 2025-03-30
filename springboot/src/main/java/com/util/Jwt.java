package com.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Jwt {
	private static String KEY = "MYSecret";
	
	public static void main(String[] args) {
		
	
		String jws = Jwts.builder()
				  .setIssuer("Apache-jjwt")
				  .setSubject("CSRF")
				  .claim("name", "Peter")				  
				  .setIssuedAt(new Date())				  
				  .setExpiration(new Date( System.currentTimeMillis() + 1000 * 60 ))  //  1 minute
				  .signWith(SignatureAlgorithm.HS256, KEY)
				  .compact();
		System.out.println(" jwt " + jws);
	}

}

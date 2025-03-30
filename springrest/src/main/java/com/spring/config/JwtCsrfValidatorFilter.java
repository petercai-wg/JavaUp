package com.spring.config;



import java.io.IOException;
import java.time.Instant;

import java.time.ZoneId;
import java.util.Arrays;


import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.services.JWTUtil;

public class JwtCsrfValidatorFilter extends OncePerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(JwtCsrfValidatorFilter.class);

	public static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

	public static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,     FilterChain filterChain) throws ServletException, IOException {
        // NOTE: A real implementation should have a nonce cache so the token cannot be reused        
    	
    	log.info("Filter request: " + request.getServletPath() );
        if (
            // only care if it's a POST
            //"POST".equals(request.getMethod()) &&request.getServletPath()
            // ignore if the request path is in our list
        	!	Arrays.asList( SecurityConfiguration.IgnoreAntMatchers ).contains(request.getServletPath() ) 
           
        ) {
        	
        	String csrfToken = request.getHeader(DEFAULT_CSRF_HEADER_NAME);
    		if (csrfToken == null) {
    			csrfToken = request.getParameter(DEFAULT_CSRF_PARAMETER_NAME);
    		}
        	
        	log.info("get JWT token from submit: " + csrfToken );
            // CsrfFilter already made sure the token matched. 
            // Here, we'll make sure it's not expired
        	if(csrfToken!= null ) {	
		        	JWTUtil jwt = new JWTUtil(csrfToken);
		        	
		        	if( jwt.isTokenValid () ){
		//		        	String subject = (String) jwt.getClaimAttribute(Claims.SUBJECT);
		//		        	int expiry = (Integer) jwt.getClaimAttribute(Claims.EXPIRATION);
				           String subject = jwt.getSubject();
				           Long expiry = jwt.getExiryDate().getTime();
				            log.info("JWT subject : " + subject + ", expiry at " + Instant.ofEpochMilli( expiry ).atZone(ZoneId.systemDefault()).toLocalDateTime() );
		        	}else{
		          
		                // most likely an ExpiredJwtException, but this will handle any
		            	log.error("JWT Exception : " );                
		                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		                RequestDispatcher dispatcher = request.getRequestDispatcher("expired-jwt");
		                dispatcher.forward(request, response);
		            }
        	}
        }else{
        	log.info("JWT validator by pass.....");
      
        }
        filterChain.doFilter(request, response);
    }
}

package com.config;

import java.io.IOException;
import java.util.Arrays;




import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.filter.OncePerRequestFilter;



/**
 * 
 * 
 *  This will act like a session validator in httpsessionless situation 
 */
public class MyHttpRequestFilter extends OncePerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(MyHttpRequestFilter.class);
	
	public static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

	public static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";
	

	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,     FilterChain filterChain) throws ServletException, IOException {
         
 
    	 if (
    	        ! (request.getServletPath().contains("/webjars/") || request.getServletPath().endsWith(".css") ||  request.getServletPath().endsWith(".js") )
    	           
    	        ){
    		 
    	       	String authorizationToken = request.getHeader(DEFAULT_CSRF_HEADER_NAME);
        		if (authorizationToken == null) {
        			authorizationToken = request.getParameter(DEFAULT_CSRF_PARAMETER_NAME);
        		}
    		    
		    	log.info("Filter request: " + request.getServletPath() + " ,  authorizationToken " + authorizationToken );
		    	
		    
		    
        }

        filterChain.doFilter(request, response);
    }
}

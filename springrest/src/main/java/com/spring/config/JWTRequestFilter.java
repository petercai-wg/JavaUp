package com.spring.config;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.services.JWTUtil;
///  for Restful api JWT authentication 
@Component
public class JWTRequestFilter extends OncePerRequestFilter {

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
				throws ServletException, IOException {

			final String requestTokenHeader = request.getHeader("Authorization");

			String username = null;
			
            if (requestTokenHeader != null ) {
	                JWTUtil jwt = new JWTUtil(requestTokenHeader);
	                logger.info("JWTRequestFilter request.getHeader(Authorization)" + requestTokenHeader);
		    		if( jwt.isTokenValid())
		    			username = jwt.getSubject();
			
			} else {
				logger.warn("JWT Token not in header Authorization");
				
			}

			// Once we get the token validate it.
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = new User(username,"pwd***", new ArrayList<>());

				// if token is valid configure Spring Security to manually set
				// authentication
	
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// After setting the Authentication in the context, we specify
					// that the current user is authenticated. So it passes the
					// Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			}
			
			chain.doFilter(request, response);
		}

	
}

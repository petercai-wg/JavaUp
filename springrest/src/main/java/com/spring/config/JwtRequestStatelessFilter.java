package com.spring.config;

import java.io.IOException;
import java.util.Arrays;




import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.services.JWTUtil;

/**
 * 
 * 
 *  This will act like a session validator in httpsessionless situation 
 */
public class JwtRequestStatelessFilter extends OncePerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(JwtRequestStatelessFilter.class);
	
	public static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

	public static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";
	
	@Autowired
	FilterChainProxy chainproxy;
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,     FilterChain filterChain) throws ServletException, IOException {
         
    	if( chainproxy != null)
    	System.out.println(request.getServletPath() + " filter " + chainproxy.getFilters(request.getServletPath()) );
    	 if (
    	            // only care if it's a POST
    	            //"POST".equals(request.getMethod()) &&request.getServletPath()
    	            // ignore if the request path is in our list
    	        	!	Arrays.asList( SecurityConfiguration.IgnoreAntMatchers ).contains(request.getServletPath() ) || 
    	        	"/login".equals(request.getServletPath())
    	           
    	        ){
    		 
    	       	String authorizationToken = request.getHeader(DEFAULT_CSRF_HEADER_NAME);
        		if (authorizationToken == null) {
        			authorizationToken = request.getParameter(DEFAULT_CSRF_PARAMETER_NAME);
        		}
    		    
		    	log.info("Filter request: " + request.getServletPath() + " authorizationToken " + authorizationToken );
		    	
		    
		    	if(authorizationToken != null )  { 		
		    		JWTUtil jwt = new JWTUtil(authorizationToken);
		    		
		    		if( jwt.isTokenValid()){
		    			
		    			if( SecurityContextHolder.getContext().getAuthentication() == null){
		    				String username = (String) request.getParameter("username");
		    				String password = (String) request.getParameter("password");
		    				
		    				if( username == null){
		    					username = "bob";
		    					password= "bob";
		    				}
		    				
		    				log.info("Filter add getAuthentication: " + username + " / " + password );
		    				
		    				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,password);
		    				authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
		    				
		    				//  make an authentication object
		    				SecurityContextHolder.getContext().setAuthentication(authToken);
		    				
		    			}
		    		}else{
			    		log.error("Filter validate Exception : " );                
		                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		                RequestDispatcher dispatcher = request.getRequestDispatcher("expired-jwt");
		                dispatcher.forward(request, response);
		    		}
		    
		    	}
           
        }else{
        	log.info("JWTRequest filter by pass.....");
        }

        filterChain.doFilter(request, response);
    }
}

package com.spring.services;


import java.util.HashMap;
import java.util.Map;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.jsonwebtoken.Claims;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Service;

@Service
public class JWTCsrfTokenRepository implements CsrfTokenRepository {

    private static final Logger log = LoggerFactory.getLogger(JWTCsrfTokenRepository.class);
    private String sessionAttributeName = JWTCsrfTokenRepository.class.getName().concat(".CSRF_TOKEN");
    
    private String DEFAULTLOGIN = "logindefault";
   
	@Override
    public CsrfToken generateToken(HttpServletRequest request) {
       
        Map<String, Object> claims = new HashMap<String, Object>();
        
       // String id = UUID.randomUUID().toString().replace("-", "");
       // claims.put("id", id);
//       if(request.getUserPrincipal() != null ){       
//    	   claims.put(Claims.SUBJECT, request.getUserPrincipal().getName());
//       }
        String username = DEFAULTLOGIN;
        if( request.getParameter("username") != null){        	
        	 username =  request.getParameter("username");
        	 log.info("Get username from request.getParameter " + username);
        }else if( request.getAttribute("username") != null){        	
	       	 username = (String) request.getAttribute("username");
	       	 log.info("Get username from request.getAttribute " + username);
        }
        	
        if( SecurityContextHolder.getContext().getAuthentication() != null && username.equals(DEFAULTLOGIN)){
        	username = SecurityContextHolder.getContext().getAuthentication().getName();
        	log.info("Get username from SecurityContextHolder " + username);
        }
	      
	     
        claims.put(Claims.EXPIRATION, System.currentTimeMillis() + (1000* 60 * 10) );  // 
       
        String token = JWTUtil.generateToken(claims,username );     
        log.info("Generated token " + token);
        
        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token);
    }

	

	@Override
	public CsrfToken loadToken(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		CsrfToken token =(CsrfToken) session.getAttribute(this.sessionAttributeName);
//		if( token != null){
//			log.info(request.getServletPath() + "  loadToken token " + token.getToken());
//			JWTUtil jwt = new JWTUtil( token.getToken());
//			if( jwt.isTokenValid() && DEFAULTLOGIN.equals(jwt.getSubject()) && ! request.getServletPath().equals("/login") ){
//				token = generateToken(request);
//				log.info("loadToken re-generateToken" );
//			}
//		}else {
//			log.info("loadToken token null" );
//		}
		return token;
	}

	@Override
	public void saveToken(CsrfToken token, HttpServletRequest request,			HttpServletResponse response) {
		

		if (token == null) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(this.sessionAttributeName);
			}
		}
		else {
			 log.info("save token " +  token.getToken());
			HttpSession session = request.getSession();
			session.setAttribute(this.sessionAttributeName, token);
		}
		
	}

	
}
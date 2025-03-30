package com.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.spring.services.JWTCookieCsrfTokenRepository;
import com.spring.services.JWTCsrfTokenRepository;

@Configuration
@EnableWebSecurity 

public class SecurityConfiguration extends WebSecurityConfigurerAdapter { 
	 private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
	
	 @Autowired
	 private JWTCsrfTokenRepository jwtCsrfTokenRepository;
		
	 @Autowired
	 private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;	 
	 
	 
	 
	 public static  String[] IgnoreAntMatchers = {
			"/favicon.ico", "/images/**", "/*.css", "/logout","/authenticate"
		    };
	 
	 
	  @Override
	  public void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.ldapAuthentication()
	        .userDnPatterns("uid={0},ou=people")
	        .userSearchBase("ou=people").userSearchFilter("(uid={0})")
	        .groupSearchBase("ou=groups").groupSearchFilter("(uniqueMember=={0})")
	        .contextSource()
	          .url("ldap://localhost:8389/dc=springframework,dc=org")
	          .and()
	        .passwordCompare()
	          .passwordAttribute("userPassword");
	    /*
	      String userid = SecurityContextHolder.getContext().getAuthentication().getName();
	      log.info(" user " + userid + "  log in ");
	      
	      jwtCsrfTokenRepository.setUsername(userid);*/
	  }

	@Override
	protected void configure(HttpSecurity http) throws Exception { 
		http.csrf().disable().
		// dont authenticate this particular request		
		authorizeRequests()		
		.antMatchers(IgnoreAntMatchers).permitAll()
		.anyRequest().authenticated().and().
		exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
		
		//.anyRequest().permitAll()
		.and().formLogin().loginPage("/login").permitAll()
		.defaultSuccessUrl("/", true)//.permitAll()				
		.and().logout().permitAll()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));  /// use HTTP GET with logout
		// Add a filter to validate the tokens with every request
		http.addFilterBefore(new JWTRequestFilter(), UsernamePasswordAuthenticationFilter.class);
		
		/***---------------------------------------------------------***/
		//CSRF protection is enabled by default with Java Configuration
		///http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
		//.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		
		
	//	http.
//		addFilterAfter(new JwtRequestStatelessFilter(), CsrfFilter.class).
		//http.addFilterBefore( new JwtRequestStatelessFilter(), UsernamePasswordAuthenticationFilter.class).
//		csrf().csrfTokenRepository(JWTCookieCsrfTokenRepository.withHttpOnlyFalse()).
		
	/*	addFilterAfter(new JwtCsrfValidatorFilter(), CsrfFilter.class).    // enable CSRF and JWT
			csrf().csrfTokenRepository(jwtCsrfTokenRepository).//ignoringAntMatchers(IgnoreAntMatchers).
			and().			
			authorizeRequests()
				//.antMatchers("/images/**", "/*.css", "/logout").permitAll()
				.antMatchers(IgnoreAntMatchers).permitAll()
				.anyRequest().authenticated()
				//.anyRequest().permitAll()
				.and().formLogin().loginPage("/login").permitAll()
				.defaultSuccessUrl("/", true)//.permitAll()				
				.and().logout().permitAll()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));  /// use HTTP GET with logout 
				//  no sessionid management 
				//.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		*/
		
				
	}

}

/**
 * if CookieCsrfTokenRepository.withHttpOnlyFalse, need to set up in JSP
 * 	<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
 * 
 * header.html
 * <meta name="_csrf" th:content="${_csrf.token}"/>
<meta name="_csrf_header" th:content="${_csrf.headerName}"/>
 * 
 */

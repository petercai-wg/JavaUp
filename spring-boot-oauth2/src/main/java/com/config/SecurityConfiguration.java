package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;




public class SecurityConfiguration extends WebSecurityConfigurerAdapter { 
	 private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
	
	 @Override
		protected void configure(HttpSecurity http) throws Exception {
			
			
		 http
		     .addFilterBefore( new MyHttpRequestFilter(), CsrfFilter.class)
		 	.csrf(c -> c
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
					)
			.authorizeRequests(a -> a
				.antMatchers("/", "/error", "/webjars/**").permitAll()
				.anyRequest().authenticated()
			)
			.exceptionHandling(e -> e
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
			)
			
			.logout(l -> l
				.logoutSuccessUrl("/").permitAll()
			)
			.oauth2Login();
			
		}
	 
}
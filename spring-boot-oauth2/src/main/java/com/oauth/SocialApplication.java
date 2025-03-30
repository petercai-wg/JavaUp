
package com.oauth;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;

import com.config.MyHttpRequestFilter;
import com.oauth.config.AppConfiguration;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

/**
 * https://github.com/   petercai-wg 
 *
 */


@SpringBootApplication
@RestController
@EnableEncryptableProperties
public class SocialApplication extends WebSecurityConfigurerAdapter {
	private static final Logger log = LoggerFactory.getLogger(SocialApplication.class);
	
	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
		String name = principal.getAttribute("name");
		log.info("Rest Request get /user OAuth2User principal " + principal.getAttribute("name") );
		
		if( SecurityContextHolder.getContext().getAuthentication() != null && name == null){
			name = SecurityContextHolder.getContext().getAuthentication().getName();
			log.info("Rest Request get /user SecurityContextHolder.getContext().getAuthentication() " + name );
		}
		 
		return Collections.singletonMap("name", name);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
	     .addFilterAfter(new MyHttpRequestFilter(), CsrfFilter.class)
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
	
	@Autowired
	AppConfiguration config;

	public static void main(String[] args) {
		System.setProperty("jasypt.encryptor.password", "peter");
		/*
		System.setProperty("jasypt.encryptor.password", "peter");
		System.setProperty("jasypt.encryptor.algorithm","PBEWITHHMACSHA512ANDAES_256");
		System.setProperty("jasypt.encryptor.iv-generator-classname", "org.jasypt.iv.RandomIvGenerator");
		*/

		SpringApplication.run(SocialApplication.class, args);
		
		
	}
	
    @PostConstruct
    public void setup(){
        log.info("Spring boot postconstruct .... ");

        config.testPrint();

        
    }

}


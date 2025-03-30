package com.oauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfiguration {
    @Value("${user.name}")
    private String username;

    @Value("${user.password}")
    private String password;

    public void testPrint() {
       
        System.out.println("Username is -------->" + username);
        System.out.println("password is --------> " +  password);
        
    }
}

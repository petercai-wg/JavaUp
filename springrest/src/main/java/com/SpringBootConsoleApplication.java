package com;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.bean.Employee;
import com.dao.EmployeeService;

/*
@SpringBootApplication
@ComponentScan(basePackages = "com.dao")
@EnableAutoConfiguration
*/
public class SpringBootConsoleApplication   implements CommandLineRunner {
 
    @Autowired    EmployeeService service;
    
    public static void main(String[] args) {
       
        SpringApplication.run(SpringBootConsoleApplication.class, args);
    	/*
    	ApplicationContext ctx = SpringApplication.run(SpringBootConsoleApplication.class, args);
    	String[] beanNames = ctx.getBeanDefinitionNames();
        
        Arrays.sort(beanNames);
 
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }*/
     
    }
  
  
    @Override
    public void run(String... args) {
    	
    	 List<Employee> list = service.getAllEmployees();
    	 for( Employee e: list){
    		 System.out.println("Employee " + e);
    	 }
    	 System.out.println("done ...");
    }
    
}

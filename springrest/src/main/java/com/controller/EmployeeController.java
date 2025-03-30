package com.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.bean.Employee;
import com.dao.EmployeeService;

 
@RestController
@CrossOrigin( "http://localhost:8080")
@RequestMapping("/rest/employee")
public class EmployeeController
{
	
	Logger logger = LogManager.getLogger(EmployeeController.class);    
	
	@Autowired
    EmployeeService service;
 
 
    /********************** Restful API for @RestController****************/ 
    //@GetMapping("/rest/employee/{id}")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id)
                                                    throws Exception {
    	
    	String loginUser = SecurityContextHolder.getContext().getAuthentication().getName();
    	
    	
    	logger.info(" @GetMapping  gEmployees .. " + id  + " , request by " + loginUser);
        Employee entity = service.getEmployeeById(id);
 
        return new ResponseEntity<Employee>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
    	String loginUser = SecurityContextHolder.getContext().getAuthentication().getName();
    	logger.info(" @GetMapping  getAllEmployees .. , by "+ loginUser);
        List<Employee> list = service.getAllEmployees();
 
        return new ResponseEntity<List<Employee>>(list, new HttpHeaders(), HttpStatus.OK);
    }    
    
    //@PostMapping("/rest/createEmployee")
    //@PutMapping("/{id}")
    @PostMapping("/update")
    //public ResponseEntity<Employee> createOrUpdateEmployee(Employee employee)
    public HttpStatus createOrUpdateEmployee(Employee employee)   throws Exception {
    	String loginUser = SecurityContextHolder.getContext().getAuthentication().getName();
    	logger.info(" @Post(  createOrUpdateEmployee .." + employee + ", requested by " + loginUser);   	
    	
        Employee updated = service.createOrUpdateEmployee(employee);
        
        logger.info("after  createOrUpdateEmployee .." + updated);
        return HttpStatus.OK;
        
        //return new ResponseEntity<Employee>(updated, new HttpHeaders(), HttpStatus.OK);
    }
 
    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteEmployeeById(@PathVariable("id") Long id) throws Exception {
    	String loginUser = SecurityContextHolder.getContext().getAuthentication().getName();
    	logger.info("  delete Employee .." + id + ", requested by " + loginUser);   
        service.deleteEmployeeById(id);
        return HttpStatus.OK;
    }
 
}
package com.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bean.Employee;


@Service
public class EmployeeService {
     
    @Autowired
    EmployeeRepository repository;
    
    public List<Employee> getEmployeeByName(String name)
    {
        List<Employee> employeeList = repository.findByFirstNameOrLastName(name);
         
        if(employeeList.size() > 0) {
            return employeeList;
        } else {
            return new ArrayList<Employee>();
        }
    }
    
    public List<Employee> getAllEmployees()
    {
        List<Employee> employeeList = repository.findAll();
         
        if(employeeList.size() > 0) {
            return employeeList;
        } else {
            return new ArrayList<Employee>();
        }
    }
     
    public Employee getEmployeeById(Long id) throws Exception
    {
        Optional<Employee> employee = repository.findById(id);
         
        if(employee.isPresent()) {
            return employee.get();
        } else {
            throw new Exception("No employee record exist for given id");
        }
    }
     
    public Employee createOrUpdateEmployee(Employee entity) throws Exception
    {
    	Employee newEntity = repository.save(entity);
        
        return entity;
    /*
    	if( entity.getId() != null){
        Optional<Employee> employee = repository.findById(entity.getId());
         
        if(employee.isPresent())
        {
            Employee newEntity = employee.get();
       
            newEntity.setFirstName(entity.getFirstName());
            newEntity.setLastName(entity.getLastName());
            newEntity.setDescription(entity.getDescription());
 
            newEntity = repository.save(newEntity);
             
            return newEntity;
        } else {
            entity = repository.save(entity);
             
            return entity;
        }*/
    }
     
    public void deleteEmployeeById(Long id) throws Exception
    {
        Optional<Employee> employee = repository.findById(id);
         
        if(employee.isPresent())
        {
            repository.deleteById(id);
        } else {
            throw new Exception("No employee record exist for given id");
        }
    }
}
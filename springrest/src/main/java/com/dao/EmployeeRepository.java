package com.dao;

import java.util.List;

import com.bean.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository   extends JpaRepository<Employee, Long> {
	 @Query("select e from Employee e where lower(e.firstName) like lower(concat('%', :search, '%')) " +
			 "or lower(e.lastName) like lower(concat('%', :search, '%'))")
     List<Employee> findByFirstNameOrLastName(@Param("search") String search);
}

/*
//@PreAuthorize("hasRole('ROLE_MANAGER')") // <1>
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {

	@Override
	//@PreAuthorize("#employee?.manager == null or #employee?.manager?.name == authentication?.name")
	Employee save(@Param("employee") Employee employee);

	@Override
	//@PreAuthorize("@employeeRepository.findById(#id)?.manager?.name == authentication?.name")
	void deleteById(@Param("id") Long id);

	@Override
	//@PreAuthorize("#employee?.manager?.name == authentication?.name")
	void delete(@Param("employee") Employee employee);
	
	//List<Employee> findAll();

}
*/

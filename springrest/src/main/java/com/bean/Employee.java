
package com.bean;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
//import javax.persistence.ManyToOne;
//import javax.persistence.Version;

//import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
    create table Employee (
         id bigint IDENTITY(1,1),        
        firstname varchar(20),
        lastname varchar(20),
         description varchar(255),
        version bigint,
        manager_id bigint,
       
         foreign key (manager_id) REFERENCES Manager(id)
    ) 

INSERT INTO 
	Employee (firstName, lastName, description,manager_id , version ) 
VALUES
  	('Lokesh', 'Gupta', 'howtodoinjava@gmail.com', 1,1)
  	('George', 'Doe', 'xyz@email.com',1, 1 )
  	
 */



@Entity

public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY  )
    private Long id;
    
    @Column(name="firstname")
    private String firstName;
    
    @Column(name="lastname")
    private String lastName;
    
    @Column(name="description", nullable=false, length=200)
    private String description;
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



    @Override
    public String toString() {
        return "EmployeeEntity [id=" + id + ", firstName=" + firstName + 
                ", lastName=" + lastName + ", description=" + description   + "]";
    }
}
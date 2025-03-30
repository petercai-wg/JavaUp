package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bean.Employee;
import com.bean.JWTRequest;
import com.bean.JWTResponse;
import com.dao.EmployeeService;
import com.spring.services.JWTUtil;


@Controller
public class HomeController {
	//Logger logger = Logger.getLogger(HomeController.class);
	//Logger logger =  LoggerFactory.getLogger(HomeController.class);
	Logger logger =  LogManager.getLogger(com.controller.HomeController.class);
	
	@GetMapping(value = {"/isolate"})
	public String forwardReactJS() {
		logger.info("forwardReactJS() to /isolate");
	    return "thymeleaf/isolate";
	}	
	
	@RequestMapping(value = "/")
	public String index() {
		return "thymeleaf/index";
	}
	
	@GetMapping(value = "/login")
	public String login() {
		return "thymeleaf/login";
	}
	
    @GetMapping("/testjsp")
    String jspPage(Model model,@RequestParam(required = false, defaultValue = "testJSP") String name) {

    	
        model.addAttribute("name", name);
        return "jsp/test";
    }	
    
    
	@PostMapping("/jspSearch")
    String jspSearch(Model model,@RequestParam String name) {

    	
        model.addAttribute("name", "search by " + name);
        return "jsp/test";
    }		
	
    @Autowired
    EmployeeService service;

 
    @RequestMapping("/employee")
    public  ModelAndView listAllEmployees(HttpServletRequest request) {
    	 HttpSession session = request.getSession(false);
    	 session.setAttribute("username", "UserName in httpSession");
    	
    	ModelAndView mav = new ModelAndView();
    	mav.setViewName("thymeleaf/list-employees");
    	mav.addObject("pageTitle", "List All Employees");
    	
        List<Employee> list = service.getAllEmployees();
        mav.addObject("name", "");
        mav.addObject("employees", list);
 
        return mav;
    }

    @RequestMapping("/searchEmployee")
    public  String searchEmployee(@RequestParam(value="name", required=true)String name, Model model) {
    	logger.info("searchEmployee ..." + name);
    	
    	model.addAttribute("pageTitle", "Search Employees");
    	
        List<Employee> list = service.getEmployeeByName(name);
        model.addAttribute("name", name);
        model.addAttribute("employees", list);
 
        return "thymeleaf/list-employees";
    }
    
    @RequestMapping("/deleteEmployee")
    public  String deleteEmployee(@RequestParam(value="id", required=true)Long id, Model model) {
    	
    	logger.info("deleteEmployee ..." + id);
    	model.addAttribute("pageTitle", "List All Employees");
    	try {
			service.deleteEmployeeById(id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	 List<Employee> list = service.getAllEmployees();
    	 model.addAttribute("name", "");
    	 model.addAttribute("employees", list);   
    	 return "thymeleaf/list-employees";
    }    
    
    @RequestMapping("/editEmployee")
    public  String editEmployee(@RequestParam(value="id", required=false)Long id, Model model) {
    	logger.info("editEmployee ..." + id);
    	Employee entity = new Employee();
    	
    	if( id != null ){
			try {
				entity = service.getEmployeeById(id);
			} catch (Exception e) {
				logger.info("editEmployee exception..." + e);
			}   
    	}
    	model.addAttribute("employee", entity); 
    	model.addAttribute("pageTitle", "Edit An Employee");
        return "thymeleaf/add-edit-employee";
    }
    
    @PostMapping("/createEmployee")
    public String createEmployee(@ModelAttribute Employee employee, Model model) {
    	logger.info("createEmployee ..." + employee);                                   
        try {
			Employee updated = service.createOrUpdateEmployee(employee);
			 model.addAttribute("employee", updated);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
      	
        model.addAttribute("pageTitle", "Update An Employee");
         return "thymeleaf/add-edit-employee";
    }    
    
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JWTRequest authenticationRequest) throws Exception {

		// Let is pass, create JWT token
		String username = authenticationRequest.getUsername();

        Map<String, Object> claims = new HashMap<String, Object>();
		
		String token = JWTUtil.generateToken(claims,username );
		logger.info("/authenticate() OK. jwt:" + token);
		return ResponseEntity.ok(new JWTResponse(token));
	}    
}

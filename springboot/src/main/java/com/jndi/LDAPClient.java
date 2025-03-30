package com.jndi;

import java.util.Hashtable;
import java.util.Scanner;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
///  ${jndi:ldap://localhost:8088/dc=example,dc=com};
public class LDAPClient {
	 
	
	public static void main(String[] args) throws NamingException {
		/*
		Hashtable env = new Hashtable(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:8088/" + LDAPRefServer.LDAP_BASE);
		env.put(Context.SECURITY_AUTHENTICATION, "none");

	
		    // Create initial context
		    DirContext ctx = new InitialDirContext(env);
		    Object obj = ctx.lookup("com.jndi.MObject");
		    // want to print all users from the LDAP server
		    System.out.println(obj);
		    ctx.close();
	*/
		   System.setProperty("log4j.configurationFile", "file:///workspace/springboot/src/main/resources/log4j2.xml");
		   ///System.setProperty("log4j.configurationFile", "resources/log4j2.xml");
			   // -Dlog4j.configurationFile=c:/workspace/springboot/src/main/resources/log4j2.xml
		    final Logger logger = LogManager.getLogger(LDAPClient.class);
		    
		    Scanner scan = new Scanner(System.in);
	    	String input = "";
	    	
	    	do {
	    		System.out.println("Enter the something to log, exit to end ");
	        
	        	input =  scan.next();
	        
	        	
		    	logger.info("Log input " + input);
	    	
	        }while(! "Exit".equalsIgnoreCase(input));
	}

}

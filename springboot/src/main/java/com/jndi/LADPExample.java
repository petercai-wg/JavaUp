package com.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
///https://www.baeldung.com/java-ldap-auth
//https://www.informit.com/articles/article.aspx?p=26231&seqNum=5
public class LADPExample {
	public static void main(String[] args) throws NamingException {
		Hashtable<String, String> environment = new Hashtable<String, String>();

		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		environment.put(Context.PROVIDER_URL, "ldap://localhost:8389/");
		
		environment.put(Context.SECURITY_AUTHENTICATION, "simple");
		environment.put(Context.SECURITY_PRINCIPAL, "cn=admin");
		environment.put(Context.SECURITY_CREDENTIALS, "secret");
		//environment.put(Context.SECURITY_AUTHENTICATION, "none");
		
		DirContext context = new InitialDirContext(environment);
		DirContext peopleContext = (DirContext) context.lookup("dc=rbccm,dc=com");
		
		//  List
		NamingEnumeration list = peopleContext.list("ou=people");

		while (list.hasMore()) {
		    NameClassPair nc = (NameClassPair)list.next();
		    System.out.println(nc);
		}
		System.out.println(" start Modify .... ");
		//  modify
		Attributes attrs = new BasicAttributes();
		attrs.put("objectClass", "CoolteckOrgPerson");
		attrs.put("email", "john@gmail.com");
		peopleContext.bind("uid=john", null,attrs);
		
		System.out.println(" start searching .... ");
		///  search 
		String filter = "(&(objectClass=person)(cn=John Doe))";
		
		SearchControls searchControls = new SearchControls();
		
		//String[] attrIDs = { "cn" };
		//searchControls.setReturningAttributes(attrIDs);
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> searchResults
		  = context.search("dc=rbccm,dc=com", filter, searchControls);
		  
		
		if (searchResults.hasMore()) {		    
		    SearchResult result = (SearchResult) searchResults.next();
		    Attributes attributes = result.getAttributes();
		    NamingEnumeration e = attributes.getAll();
		 // Loop through the attributes
		    while (e.hasMoreElements())
		    {
		
		         Attribute attr = (Attribute) e.nextElement();

		         System.out.print(attr.getID()+" = ");
		                     for (int i=0; i < attr.size(); i++)
		                     {
		                         if (i > 0) System.out.print(", ");
		                         System.out.print(attr.get(i));
		                     }
		                     System.out.println();
		          
		                
		    }
	
		    

		   
		   
		}
	
		context.close();
		System.out.println(" successfully finished  ....");
	}
}

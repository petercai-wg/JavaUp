package com.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;

public class MObject implements Referenceable, javax.naming.spi.ObjectFactory {
	
	public MObject(){
		
	}
	
	public String toString(){
		System.out.println(" Hey this is Malicious code from MOject ....");
		return "Hacker show up ";
	}

	@Override
	public Reference getReference() throws NamingException {
		Reference ref = new Reference( MObject.class.getName());
		
		
		return ref;
	}

	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx,
			Hashtable<?, ?> environment) throws Exception {
		
		return this ;
	}

}

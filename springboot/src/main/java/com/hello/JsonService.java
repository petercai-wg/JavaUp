package com.hello;



import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.ResultService;
import com.search.SearchResult;

public class JsonService {
	public static void main(String[] args){
		jsonString();
	}
	
    public static String jsonString()  {
    	
    	try{
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	
	    	ResultService s = new ResultService();
	    	ArrayList<SearchResult> lists =  s.getLists();
	    	
	    	
	        String person =   objectMapper.writeValueAsString(lists);
	        System.out.println(person);
	        return person;
    	}catch(Exception e){
    		return "";
    	}
      
    }
}

package com.search;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController { 
    @Autowired
    private ResultService service;
   
    @GetMapping("/")
    public String index(Model model) {	
    	System.out.println(" index/ ...");
    	List<SearchResult> searchResults = service.getLists();
        
        model.addAttribute("search", searchResults);
        return "/search";
    }
    
 
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@RequestParam(value = "search_input", required = false) String q, Model model) {
        List<SearchResult> searchResults = new ArrayList<SearchResult>();;
        try {
        	SearchResult s = service.getResult(q);
        	if( s != null)searchResults.add(s);

        } catch (Exception ex) {
            
        }
        model.addAttribute("search", searchResults);
        return "search_result";

    }

}

package com.search;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ResultService {
	
	private Hashtable<String, SearchResult> persons = new Hashtable<String, SearchResult>();
	private ArrayList<SearchResult> lists = new ArrayList<SearchResult>();
    
	public SearchResult getResult(String p) {
		 return persons.get(p);
	}
	
	public ArrayList<SearchResult> getLists() {
		 return lists;
	}
	 
    public ResultService() {  	
    	
    	SearchResult TedWilliams = new SearchResult();
    	SearchResult BobGibson = new SearchResult();
    	SearchResult HonusWagner = new SearchResult();
    	
    	TedWilliams.setId(1L);
        TedWilliams.setName("Ted Williams");
        TedWilliams.setYear(1954);
        TedWilliams.setRarityLevel("Very Rare");
        
        lists.add(TedWilliams);        
        persons.put("Ted",TedWilliams );

        BobGibson.setName("Bob Gibson");
        BobGibson.setYear(1959);
        BobGibson.setRarityLevel("Very Rare");
        BobGibson.setId(2L);

        
        persons.put("Bob", BobGibson);
        lists.add(BobGibson);

        HonusWagner.setName("Honus Wagner");
        HonusWagner.setYear(1909);
        HonusWagner.setRarityLevel("Rarest");
        HonusWagner.setId(3L);
        
        persons.put("Honus", HonusWagner);
        lists.add(HonusWagner);
       
    }

   
}

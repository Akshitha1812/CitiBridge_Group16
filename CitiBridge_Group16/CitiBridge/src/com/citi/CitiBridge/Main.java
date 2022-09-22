package com.citi.CitiBridge;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.AsyncEvent;

import org.json.*;
//import org.json.JSONObject;  
//import org.json.JSONArray;    
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

	
	//curl https://financialmodelingprep.com/api/v3/nasdaq_constituent?apikey=0ae0a0c916d0fb467d3d50aed85ebb3e    
	
	public class Main {

	    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
	    	Scanner s = new Scanner(System.in);
	    	/*large-cap ($10 billion or more), mid-cap ($2 billion to $10 billion), and small-cap ($300 million to $2 billion).*/
	    	System.out.println("Select which market cap data you want to see");
	    	String user_input = s.nextLine();
	    	
	    	double Large_cap = 800559000000.00;  //10 billion dollars
	    	double Medium_cap = 160111800000.00; // 2 billion dollars
	    	double Small_cap = 24016770000.00;  //300 million dollars
	    	//double Small_cap_low = 800559000000.00;
	    	
	    	HashMap <String,Double> sc = new HashMap <String,Double>();
	    	HashMap <String,Double> mc = new HashMap <String,Double>();
	    	HashMap <String,Double> lc = new HashMap <String,Double>();
	    	
	    	
		    
	    	
	    	HttpRequest request1 = HttpRequest.newBuilder()
	    			.uri(URI.create("https://stock-market-data.p.rapidapi.com/market/index/nifty-fifty"))
	    			.header("X-RapidAPI-Key", "804ee650e0mshb52fb6edfa17097p155917jsn13caf94ec228")
	    			.header("X-RapidAPI-Host", "stock-market-data.p.rapidapi.com")
	    			.method("GET", HttpRequest.BodyPublishers.noBody())
	    			.build();
	    	

	    	HttpResponse<String> response1 = null;
	    	HttpResponse<String> response2 =null;
	    	try {
	    		
	    		 response1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
	    		 //String result = EntityUtils.toString(response1.getEntity());
	    		 //JSONObject myObject = new JSONObject();
	    		 //System.out.println(response1.getClass().getSimpleName());
	    	} catch (IOException e1) {
	    		// TODO Auto-generated catch block
	    		e1.printStackTrace();
	    	} catch (InterruptedException e1) {
	    		// TODO Auto-generated catch block
	    		e1.printStackTrace();
	    	}
	    	//System.out.println(response1.body().toString());
	    	JSONParser parser = new JSONParser();  
	    	try {
	    		JSONObject json = (JSONObject) parser.parse(response1.body().toString());		
	    		//System.out.println(json.get("stocks"));
	    		JSONArray str = (JSONArray) json.get("stocks");
	    		for(int i=0;i<16;i++)
	    		{
	    			Object symbol = str.get(i);
	    			String ticker_symbol = symbol.toString();
	    			//System.out.println(ticker_symbol);
	    			//System.out.println(str.get(i));
	    			HttpRequest request = HttpRequest.newBuilder()
	    					.uri(URI.create("https://yahoofinance-stocks1.p.rapidapi.com/stock-statistics?Symbol="+str.get(i)))
	    					.header("X-RapidAPI-Key", "e7138f4862msha1f455a6645c0e9p194507jsn372e8cb341d3")
	    					.header("X-RapidAPI-Host", "yahoofinance-stocks1.p.rapidapi.com")
	    					.method("GET", HttpRequest.BodyPublishers.noBody())
	    					.build();
	    			java.lang.Thread.sleep(90);
	    			
	    			try {
	    	    		
	   	    		 response2 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
	   	    		//System.out.println(response2.body());
	   	    	} catch (IOException e1) {
	   	    		// TODO Auto-generated catch block
	   	    		e1.printStackTrace();
	   	    	} catch (InterruptedException e1) {
	   	    		// TODO Auto-generated catch block
	   	    		e1.printStackTrace();
	   	    	}
	    			
	    			JSONParser parser1 = new JSONParser();  
	    	    	try {
	    	    		JSONObject json1 = (JSONObject) parser.parse(response2.body().toString());	
	    	    	//	System.out.println(response2.body().toString());
	    	    	//	JSONArray str1 = (JSONArray) json1.get("result");
	    	    		JSONObject str1 = (JSONObject) json1.get("result");
	    	    		JSONArray str3 = (JSONArray) str1.get("quarterlyValuationMeasures");
	    	    		JSONObject str4 = (JSONObject) str3.get(0);
	    	    		//System.out.println(str4.getClass().getSimpleName());
	    	    		//System.out.println(str4);
	    	    		JSONObject x = (JSONObject) parser.parse(str4.toString());
	    	    		String y = x.toString();
	    	    		//System.out.println(x.toString().getClass().getSimpleName());
	    	    		int n=0;
	    	    		if(y.contains("value"))
	    	    		{
	    	    			 n = y.indexOf("value");
	    	    		}
	    	    		String market = y.substring(n+8, y.length()-2);
	    	    		Double marketcap = Double.parseDouble(market);
	    	    		if(marketcap > Small_cap && marketcap< Medium_cap)
	    	    		{
	    	    			sc.put(ticker_symbol,marketcap );
	    	    		}
	    	    		else if(marketcap> Medium_cap && marketcap< Large_cap)
	    	    		{
	    	    			mc.put(ticker_symbol,marketcap);
	    	    		}
	    	    		else if(marketcap > Large_cap)
	    	    		{
	    	    			lc.put(ticker_symbol,marketcap);
	    	    		}
	    	    		else
	    	    		{
	    	    			System.out.println("select correct cap");
	    	    		}
	    	    		
	    	    		//System.out.printf("%f",marketcap);
	    	    		
	    	    	//	System.out.println(marketcap);
	    		} catch (ParseException e) {
		    		 //TODO Auto-generated catch block
		    		e.printStackTrace();
		    	} 
	    	}  
	    	}
	    	finally {  
	    		//System.out.println("here");  
	    	Map<Object, Object> Smallsorted = sc.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,LinkedHashMap::new));
			Smallsorted = sc.entrySet()
			        .stream()
			        .sorted(Map.Entry.comparingByValue())
			        .collect(
			            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
			                LinkedHashMap::new));
			Smallsorted = sc
			        .entrySet()
			        .stream()
			        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
			        .collect(
			            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
			                LinkedHashMap::new));
			 if(user_input.equals("Small"))
			 {
			    System.out.println("Smallmap after sorting by values in descending order: "+ Smallsorted);
			 }
			
			Map<Object, Object> Mediumsorted = mc
			        .entrySet()
			        .stream()
			        .sorted(Map.Entry.comparingByValue())
			        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
			                LinkedHashMap::new));
			Mediumsorted = mc
			        .entrySet()
			        .stream()
			        .sorted(Map.Entry.comparingByValue())
			        .collect(
			            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
			                LinkedHashMap::new));
			Mediumsorted = mc
			        .entrySet()
			        .stream()
			        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
			        .collect(
			            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
			                LinkedHashMap::new));
			if(user_input.equals("Medium"))
			 {
			    System.out.println("Medium map after sorting by values in descending order: "+ Mediumsorted);
			 }
			
			Map<Object, Object> Largesorted = lc
			        .entrySet()
			        .stream()
			        .sorted(Map.Entry.comparingByValue())
			        .collect(
			            Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
			                LinkedHashMap::new));
			Largesorted = lc
			        .entrySet()
			        .stream()
			        .sorted(Map.Entry.comparingByValue())
			        .collect(
			            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
			                LinkedHashMap::new));
			Largesorted = lc
			        .entrySet()
			        .stream()
			        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
			        .collect(
			            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
			                LinkedHashMap::new));
			if(user_input.equals("Large"))
			 {
			  System.out.println("Large map after sorting by values in descending order: "+ Largesorted);
			 }
			  //System.out.println(sc);
			//System.out.println(mc);
			//System.out.println(lc);
	    	
	    }
	    }
	    	}
	    	
	    	




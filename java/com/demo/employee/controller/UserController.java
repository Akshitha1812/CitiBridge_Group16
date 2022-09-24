package com.demo.employee.controller;

import com.demo.employee.entity.User;
import com.demo.employee.service.UserService;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
//@Controller
public class UserController {
    @Autowired
    UserService userService;

	 String userLogin(String username,String password) 
	 { 
		 boolean res=userService.authenticate(username,password); 
		 if(res==true) 
		 { 
			 return "success"; 
	     }
		 else 
		 { 
			 return "failure"; 
		 } 
	  }
	 
    
    //@GetMapping("/login")
    //public String login(/*Model model*/)
    //{
    	//model.addAttribute("user",new User());
    	//return "login.html";
    //}
    @RequestMapping("/")
    public ModelAndView home(@ModelAttribute("userForm") User user,
            Map<String, Object> model)
    {
    	ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
    
	@GetMapping("/login.html") 
	public String showLogin(@RequestParam String username, @RequestParam String password,Model object ) 
	{ 
		userLogin(username,password);
		System.out.println(username);
	     System.out.println(password);
		return "login"; 
	}
    @PostMapping("/login.html")
    public String login(@ModelAttribute(name="userLogin") User login, Model m) {
     String uname = login.getUsername();
     String pass = login.getPassword();
     System.out.println("username: " + uname);
     System.out.println("password: " + pass);
     if(uname.equals("Navya_1") && pass.equals("navya123")) {
      m.addAttribute("uname", uname);
      m.addAttribute("pass", pass);
      return "welcome";
     }
     m.addAttribute("error", "Incorrect Username & Password");
     return "login";
     
    }
    
    
    
    //@PostMapping("/loginProcess")
	/*
	 * @RequestMapping(value = "/loginProcess", method = {RequestMethod.GET,
	 * RequestMethod.POST}) public String loginProcess(HttpServletRequest request,
	 * HttpServletResponse response,
	 * 
	 * @ModelAttribute("login") User login) throws IOException { //ModelAndView mav
	 * = null;
	 * 
	 * //boolean user =
	 * userService.authenticate(login.getUsername(),login.getPassword());
	 * 
	 * //if (user) { // return "success"; //} //else { // return "failure"; //}
	 * response.setContentType("text/html"); //PrintWriter out=response.getWriter();
	 * String u_id=request.getParameter("uname"); String
	 * pswd=request.getParameter("psw"); //boolean flag=false; boolean
	 * ret=userService.authenticate(u_id, pswd);
	 * 
	 * if(ret) { return "success"; } else { return "failure"; } }
	 */

    
    @RequestMapping(value = "/loginProcess",method = RequestMethod.POST)
    public String processRegistration(@ModelAttribute("userForm") User user,
            Map<String, Object> model) {
         
        // implement your own registration logic here...
         
        // for testing purpose:
        System.out.println("username: " + user.getUsername());
        System.out.println("password: " + user.getPassword());
         
        return "RegistrationSuccess";
    }
    
    @RequestMapping("/stocks")
    public ModelAndView getStocks()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("stocks");
        //modelAndView.getView().
        return modelAndView;
    }
	/*
	 * @GetMapping("/greeting") public String greetingForm(Model model) {
	 * model.addAttribute("greeting", new Greeting()); return "greeting"; }
	 * 
	 * @PostMapping("/greeting") public String greetingSubmit(@ModelAttribute
	 * Greeting greeting, Model model) { model.addAttribute("greeting", greeting);
	 * return "result"; }
	 */
    @RequestMapping(value = "/getStocks",method = RequestMethod.POST, params = { "type" })
    public void getStocks(ModelMap modelMap,@RequestParam(value = "type") String user_input) throws InterruptedException, ParseException {
        double Large_cap = 800559000000.00;  //10 billion dollars
        double Medium_cap = 160111800000.00; // 2 billion dollars
        double Small_cap = 24016770000.00;  //300 million dollars
        //double Small_cap_low = 800559000000.00;

        HashMap<String, Double> sc = new HashMap<String, Double>();
        HashMap<String, Double> mc = new HashMap<String, Double>();
        HashMap<String, Double> lc = new HashMap<String, Double>();


        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("https://stock-market-data.p.rapidapi.com/market/index/nifty-fifty"))
                .header("X-RapidAPI-Key", "632d917c00msheaf31762a3a44e4p162840jsn70f4844375b3")
                .header("X-RapidAPI-Host", "stock-market-data.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();


        HttpResponse<String> response1 = null;
        HttpResponse<String> response2 = null;
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
        JSONParser parser = new JSONParser(response1.body().toString());
        try {
            JSONObject json = (JSONObject) parser.parse();
            //System.out.println(json.get("stocks"));
            JSONArray str = (JSONArray) json.get("stocks");
            //System.out.println(str);
            for (int i = 0; i < 10; i++) {
                Object symbol = str.get(i);
                String ticker_symbol = symbol.toString();
                //System.out.println(ticker_symbol);
                //System.out.println(str.get(i));
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://yahoofinance-stocks1.p.rapidapi.com/stock-statistics?Symbol=" + str.get(i)))
                        .header("X-RapidAPI-Key", "634c1ebef2msh96c03cd1c7996cep164470jsn8ecb6928c043")
                        .header("X-RapidAPI-Host", "yahoofinance-stocks1.p.rapidapi.com")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                Thread.sleep(90);

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

                JSONParser parser1 = new JSONParser(response2.body().toString());
                try {
                    JSONObject json1 = (JSONObject) parser1.parse();
                    System.out.println(response2.body().toString());
                    //	JSONArray str1 = (JSONArray) json1.get("result");
                    JSONObject str1 = (JSONObject) json1.get("result");
                    JSONArray str3 = (JSONArray) str1.get("quarterlyValuationMeasures");
                    JSONObject str4 = (JSONObject) str3.get(0);
                    //System.out.println(str4.getClass().getSimpleName());
                    //	System.out.println(str4);
                    JSONParser parser3=new JSONParser(str4.toString());
                    JSONObject x = (JSONObject) parser3.parse();
                    String y = x.toString();
                    //System.out.println(x.toString().getClass().getSimpleName());
                    int n = 0;
                    if (y.contains("value")) {
                        n = y.indexOf("value");
                    }
                    String market = y.substring(n + 8, y.length() - 2);
                    Double marketcap = Double.parseDouble(market);
                    //	add_to_database(ticker_symbol,BigDecimal.valueOf(marketcap));
                    if (marketcap > Small_cap && marketcap < Medium_cap) {
                        sc.put(ticker_symbol, marketcap);
                    } else if (marketcap > Medium_cap && marketcap < Large_cap) {
                        mc.put(ticker_symbol, marketcap);
                    } else if (marketcap > Large_cap) {
                        lc.put(ticker_symbol, marketcap);
                    } else {
                        System.out.println("select correct cap");
                    }

                    //System.out.printf("%f",marketcap);

                    //	System.out.println(marketcap);
                } catch (ParseException e) {
                    //TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } finally {
            //System.out.println("here");
            Map<Object, Object> Smallsorted = sc.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
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
            if (user_input.equals("Small")) {
                // System.out.println("Smallmap after sorting by values in descending order: "+ Smallsorted);
                System.out.println("For Large Market Cap:");
                //  System.out.println("Large map after sorting by values in descending order: "+ Largesorted);
                int count = 0;
                for (Map.Entry<Object, Object> mapElement : Smallsorted.entrySet()) {
                    if (count < 5 && Smallsorted.size()>5) {
                        Object key = mapElement.getKey();

                        // Adding some bonus marks to all the students
                        //    Object value = mapElement.getValue();

                        // Printing above marks corresponding to
                        // students names
                        System.out.println(key);
                    } else {
                    	for (Map.Entry<Object, Object> mapElements : Smallsorted.entrySet()) {
                    		Object key = mapElements.getKey();
                    		System.out.println(key);
                    	}
                    }
                    count++;
                    modelMap.put("response", Smallsorted);
                }
            }
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
        if (user_input.equals("Medium")) {
            // System.out.println("Medium map after sorting by values in descending order: "+ Mediumsorted);
            System.out.println("For Medium Market Cap:");
            //  System.out.println("Large map after sorting by values in descending order: "+ Largesorted);
            for (Map.Entry<Object, Object> mapElement : Mediumsorted.entrySet()) {
            	int count = 0;
            	 if (count < 5 && Mediumsorted.size()>5) {
            		 
            	
                Object key = mapElement.getKey();

                // Adding some bonus marks to all the students
               // Object value = mapElement.getValue();

                // Printing above marks corresponding to
                // students names
               // System.out.println(key + " : " + value);
            	 }
            	 else {
            		 for (Map.Entry<Object, Object> mapElements : Mediumsorted.entrySet()) {
                 		Object key = mapElements.getKey();
                 		System.out.println(key);
                 	}
            	 count ++;
            	 modelMap.put("response", Mediumsorted);

            }
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
        if (user_input.equals("Large")) {
            System.out.println("For Large Market Cap:");
            //  System.out.println("Large map after sorting by values in descending order: "+ Largesorted);
            int count = 0;
            for (Map.Entry<Object, Object> mapElement : Largesorted.entrySet()) {
                if (count < 5 && Largesorted.size()>5) {
                    Object key = mapElement.getKey();

                    // Adding some bonus marks to all the students
                    // Object value = mapElement.getValue();

                    // Printing above marks corresponding to
                    // students names
                    System.out.println(key);
                } else {
                	for (Map.Entry<Object, Object> mapElements : Largesorted.entrySet()) {
                		Object key = mapElements.getKey();
                		System.out.println(key);
                	}
                	}
                    break;
                }
                count++;
                modelMap.put("response", Largesorted);
            }
        }
      //  System.out.println(Largesorted);
      //  modelMap.put("response", topFive);

    }


}

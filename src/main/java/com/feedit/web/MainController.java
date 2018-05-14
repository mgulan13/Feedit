package com.feedit.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root(Model model, Principal principal) {
    	if (principal == null) { 
    		return "login";
    	}
    	
    	return "articles";
    }

}

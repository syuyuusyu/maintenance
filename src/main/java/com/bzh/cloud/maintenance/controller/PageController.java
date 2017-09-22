package com.bzh.cloud.maintenance.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
//@RequestMapping(value = "/api")
public class PageController {
	
	@RequestMapping(value="/entityPage")
	public String entity(Model model,HttpServletRequest request){
		String token=(String) request.getSession().getAttribute("token");
		System.out.println(token);
		return "entityConf";
	}



	@RequestMapping(value="/index")
	public String index(Model model,String token,HttpServletRequest request){
		System.out.println(token);
		request.getSession().setAttribute("token", token);
		model.addAttribute("token", token);
		return "index";
	}

	@RequestMapping(value="/loginIndex")
	public String login(Model model){
		return "login";
	}


}

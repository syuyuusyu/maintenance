package com.bzh.cloud.maintenance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
//@RequestMapping(value = "/api")
public class PageController {
	
	@RequestMapping(value="/entityPage")
	public String entity(Model model){
		return "entityConf";
	}



	@RequestMapping(value="/index")
	public String index(Model model){
		return "index";
	}


}

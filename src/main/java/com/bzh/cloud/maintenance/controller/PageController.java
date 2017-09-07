package com.bzh.cloud.maintenance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping(value = "/api")
public class PageController {
	
	@RequestMapping(value="/entityPage")
	public String entity(Model model){
		System.out.println("sdsdsds");
		return "entity";
	}

}

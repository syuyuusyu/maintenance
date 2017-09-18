package com.bzh.cloud.maintenance.controller;


//import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient("bzh-gate")
public interface AuthController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login();

}

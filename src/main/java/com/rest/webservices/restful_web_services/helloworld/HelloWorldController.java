package com.rest.webservices.restful_web_services.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//Controller
@RestController
public class HelloWorldController {
	
	
	//GET
	//URI - /hello-world
	//method - "Hello World
//	@RequestMapping(method=RequestMethod.GET, path = "/hello-world")
	@GetMapping(path = "hello-world")
	public String hellloWorld() {
		return "Hello World";
	}	
	
	// create a bean and return it back
	//hello-world-bean
	@GetMapping(path = "hello-world-bean")
	public HelloWorldBean hellloWorldBean() {
		return new HelloWorldBean("Hello World Bean");
	}
	
	//hello-world/path-variable/in28minutes
	@GetMapping(path = "hello-world/path-variable/{name}")
	public HelloWorldBean hellloWorldPathVariable(@PathVariable String name) {
		return new HelloWorldBean(String.format("Hello World Bean %s", name));
	}	
}

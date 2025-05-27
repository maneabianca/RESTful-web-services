package com.rest.webservices.restful_web_services.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

//Controller
@RestController
public class HelloWorldController {
	
	@Autowired
	private MessageSource messageSource;
	
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
	
	/*
	 * Method 1
	 * 
	@GetMapping(path = "hello-world-internationalized")
	public String hellloWorldInternationalized(@RequestHeader(name="Accept-Language", required=false) Locale locale) {
		return messageSource.getMessage("good.morning.message",null, locale);
	}
	*/
	
	// Method 2
	@GetMapping(path = "hello-world-internationalized")
	public String hellloWorldInternationalized() {
		return messageSource.getMessage("good.morning.message",null, LocaleContextHolder.getLocale());
	}
}

package com.in28minutes.rest.webservices.restfulwebservices.helloworld;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	
	private MessageSource messageSource;
	
	public HelloWorldController(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/*
	 *  Not precise approach
	 * @RequestMapping(method = RequestMethod.GET, path = "/hello-world") public
	 * String helloWorld() { return "Hello World"; }
	 */
	
//	Best approach
	@GetMapping(path = "/hello-world")
	public String helloWorld() {
		return "Hello World";
	}
	
	// Returning bean and it response in json format -> Simply Conversion
	@GetMapping(path = "/hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("Hello World");
	}
	
	/*
	 *  PathParameter -> is concept of sending variables in the url e.g {name}
	 * 		@PathVariable -> This annotation must be specify in the method as argument speciftying that the type and variable name
	 * 										must match with the variable in the URI
	 * */
	@GetMapping(path = "/hello-world/path-variable/{name}")
	public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
		return new HelloWorldBean(String.format("Hello World %s", name));
	}
	
	@GetMapping(path = "/hello-world-Internalized")
	public String helloWorldInternalized() {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage("good.morning.message", null, "Default Message", locale);
//		return "Hello World";
	}
}

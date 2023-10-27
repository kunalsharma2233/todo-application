package com.in28minutes.rest.webservices.restfulwebservices.filtering;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class FilteringController {
	
//	static filtering concept
	@GetMapping("/filter")
	public MappingJacksonValue filterFeilds() {
//		Dynamic Filtering example
		SomeBean someBean = new SomeBean("value1", "value2", "value3");
		MappingJacksonValue  mappingJacksonValue = new MappingJacksonValue(someBean);	
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("value1","value3");
		FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
		mappingJacksonValue.setFilters(filters);
		return mappingJacksonValue;
	}
	
	@GetMapping("/filter-list")
	public MappingJacksonValue filterFields() {
		List<SomeBean> list = Arrays.asList(new SomeBean("value1", "value2", "value3"), 
				new SomeBean("Value4","value5","value6"));
		
		MappingJacksonValue  mappingJacksonValue = new MappingJacksonValue(list);	
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("value2","value3");
		FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
		mappingJacksonValue.setFilters(filters);
		
		return mappingJacksonValue;
	}
}

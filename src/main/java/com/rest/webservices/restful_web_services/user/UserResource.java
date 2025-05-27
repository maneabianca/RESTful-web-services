package com.rest.webservices.restful_web_services.user;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import jakarta.validation.Valid;

@RestController
public class UserResource {

	@Autowired
	private UserDaoService service;
	
	//GET /users
	@GetMapping("/users")
	public List<User> retrieveAllUsers(){
		return service.findAll();
	}


	//GET /users
	//We are returning a resource which has both data and links
	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		
		User user = service.findOne(id);
		if(user == null) {
			throw new UserNotFoundException("id- "+ id);
		}
		
		// HATEOAS -> easily add links using the methods
		// "all-users", SERVER_PATH + "/users"
		EntityModel<User> model = EntityModel.of(user);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		model.add(linkTo.withRel("all-users"));
		
		return model;
		
		/*
		//Dynamic Filtering + adding annotation in User class => @JsonFilter("SomeBeanFilter")
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name");		
		FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);		
		MappingJacksonValue mapping = new MappingJacksonValue(user);
		mapping.setFilters(filters);
		
		
		return mapping;
		*/
	}
	
	// input - details of user
	// output - CREATED status & Return the created URI
	
	//POST /users
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		
		User savedUser = service.save(user);
		
		// /user/{id} -> savedUser.getId()
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()    // return the current request URI
										.path("/{id}")					   // appending /{id}
										.buildAndExpand(savedUser.getId()) // replacing with savedUser.getId()
										.toUri();
		return ResponseEntity.created(location).build(); //in the header of the response
	}
	
	// Delete /users/{id}
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {

		User user = service.deleteById(id);

		if (user == null) {
			throw new UserNotFoundException("id- " + id);
		}
	}

}

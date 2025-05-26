package com.rest.webservices.restful_web_services.user;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserResource {

	@Autowired
	private UserDaoService service;
	
	//GET /users
	@GetMapping("/users")
	public List<User> retrieveAllUser(){
		return service.findAll();
	}

	//GET /users
	@GetMapping("/users/{id}")
	public User retrieveUser(@PathVariable int id) {
		return service.findOne(id);
	}
	
	// input - details of user
	// output - CREATED status & Return the created URI
	
	//POST /users
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User savedUser = service.save(user);
		
		// /user/{id} -> savedUser.getId()
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()    // return the current request URI
										.path("/{id}")					   // appending /{id}
										.buildAndExpand(savedUser.getId()) // replacing with savedUser.getId()
										.toUri();
		return ResponseEntity.created(location).build(); //in the header of the response
	}
	
}

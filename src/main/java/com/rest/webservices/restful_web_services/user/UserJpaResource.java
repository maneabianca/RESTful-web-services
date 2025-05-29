package com.rest.webservices.restful_web_services.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers(){
		return userRepository.findAll();
	}


	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable long id) {
		
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("id- "+ id);
		}
		
		// HATEOAS -> easily add links using the methods
		// "all-users", SERVER_PATH + "/users"
		EntityModel<User> model = EntityModel.of(user.get());
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		model.add(linkTo.withRel("all-users"));
		
		return model;
	}
	
	
	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		
		User savedUser = userRepository.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()    // return the current request URI
										.path("/{id}")					   // appending /{id}
										.buildAndExpand(savedUser.getId()) // replacing with savedUser.getId()
										.toUri();
		return ResponseEntity.created(location).build(); //in the header of the response
	}
	
	// Delete /users/{id}
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable long id) {
		userRepository.deleteById(id);
	}
	
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrieveAllPostForUser(@PathVariable long id){
		
		Optional<User> user = userRepository.findById(id);		
		if(!user.isPresent()) {
			throw new UserNotFoundException("id- "+ id);
		}
		
		return user.get().getPosts();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<User> createUPost(@PathVariable long id, @RequestBody Post post) {
		
		Optional<User> userOptional = userRepository.findById(id);		
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id- "+ id);
		}
		
		User user = userOptional.get();
		//add a post
		post.setUser(user);
		postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()    // return the current request URI
										.path("/{id}")					   // appending /{id}
										.buildAndExpand(post.getId()) // replacing with savedUser.getId()
										.toUri();
		return ResponseEntity.created(location).build(); //in the header of the response
	}

}

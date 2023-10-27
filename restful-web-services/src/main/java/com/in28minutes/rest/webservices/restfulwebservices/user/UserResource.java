package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

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
public class UserResource {
	
	private UserDaoService userDaoService;
	
	public UserResource(UserDaoService userDaoService) {
		this.userDaoService = userDaoService;
	}
	
	//GET /users	
	@GetMapping(path = "/users")
	public List<User> retrieveAllUsers() {
		return userDaoService.findAll();
	}
	
//	For Hateoas -> to generate links as a part of the response we need concepts of hateoas to understand
//	EntityModel
//	WebMvcLinkBuilder
	
	@GetMapping(path = "/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		User user  = userDaoService.findOne(id);
		if (user == null) 
 			throw new UserNotFoundException("id:" +id); 
		
		EntityModel<User> entityModel = EntityModel.of(user);
		
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
	
		return entityModel;
	}
	
	@DeleteMapping(path = "/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userDaoService.deleteById(id);
	}
	
//	@RequestBody annotation => Basically accepts the value body and it is declared in the method argument
//	To check weather the user is created then we have to use existing uri path with associated method e,g, @PostMapping(path = "/user"
//	Post /users
	@PostMapping(path = "/users")
	public ResponseEntity<User> creatUser(@Valid @RequestBody User user)  {
		User savedUser = userDaoService.save(user);
		// /users/4 => /users/{id}, user.getID
		URI location = ServletUriComponentsBuilder
											.fromCurrentRequest()
											.path("/{id}")
											.buildAndExpand(savedUser.getId())
											.toUri();
		return ResponseEntity.created(location).build();
	}
	
}

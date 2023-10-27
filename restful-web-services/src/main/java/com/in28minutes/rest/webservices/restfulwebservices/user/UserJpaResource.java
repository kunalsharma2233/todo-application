package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.in28minutes.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {

	private UserRepository repository;

	private PostRepository postRepository;

	public UserJpaResource(UserRepository repository, PostRepository postRepository) {
		this.repository = repository;
		this.postRepository = postRepository;
	}

	// GET /users
	@GetMapping(path = "/jpa/users")
	public List<User> retrieveAllUsers() {
		return repository.findAll();
	}

//	For Hateoas -> to generate links as a part of the response we need concepts of hateoas to understand
//	EntityModel
//	WebMvcLinkBuilder

	@GetMapping(path = "/jpa/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		Optional<User> user = repository.findById(id);
		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		EntityModel<User> entityModel = EntityModel.of(user.get());

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));

		return entityModel;
	}

	@DeleteMapping(path = "/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		repository.deleteById(id);
	}

	@GetMapping(path = "/jpa/users/{id}/posts")
	public List<Post> retrivePostById(@PathVariable int id) {
		Optional<User> user = repository.findById(id);

		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		return user.get().getPosts();
	}

//	@RequestBody annotation => Basically accepts the value body and it is declared in the method argument
//	To check weather the user is created then we have to use existing uri path with associated method e,g, @PostMapping(path = "/user"
//	Post /users
	@PostMapping(path = "/jpa/users")
	public ResponseEntity<User> creatUser(@Valid @RequestBody User user) {
		User savedUser = repository.save(user);
		// /users/4 => /users/{id}, user.getID
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@PostMapping(path = "/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post) {
		Optional<User> user = repository.findById(id);

		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		post.setUser(user.get());

		Post savedPost = postRepository.save(post);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId())
				.toUri();
		return ResponseEntity.created(location).build();

	}

}

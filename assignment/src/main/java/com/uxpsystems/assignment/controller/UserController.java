package com.uxpsystems.assignment.controller;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.service.UserService;

/**
 * REST controller for managing User.
 */
@RestController
@RequestMapping("/api")
// @Component
public class UserController {

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * GET /users : get all the users.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of users in
	 *         body
	 */
	@GetMapping("/users")
	public List<User> getUsers() {
		log.debug("REST request to get all Users");
		return userService.getAll();
	}

	/**
	 * GET /user/:id : get the "id" user.
	 *
	 * @param id
	 *            the id of the user to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the user,
	 *         or exception with status 500 (Internal Server Error) user not found
	 * @throws Exception
	 */
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUser(@PathVariable Long id) throws Exception {
		log.debug("REST request to get User : {}", id);
		Optional<User> user = userService.findById(id);
		User result = user.orElse(null);
		if (result == null) {
			throw new Exception("User not found");
		}
		return new ResponseEntity<User>(result, HttpStatus.OK);
	}

	/**
	 * DELETE /user/:id : delete the "id" user.
	 *
	 * @param id
	 *            the id of the user to delete
	 * @return the boolean true if deleted or exception with status 500 (Internal Server Error) no class user entity with ID exists
	 */
	@DeleteMapping("/user/{id}")
	public Boolean deleteUser(@PathVariable Long id) {
		log.debug("REST request to delete User : {}", id);
		userService.delete(id);
		return true;
	}

	/**
	 * POST /user : Create a new user.
	 *
	 * @param user
	 *            the user to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new user, or exception if the user has already an ID
	 * @throws Exception
	 */
	@PostMapping("/user")
	public ResponseEntity<User> createUser(@RequestBody User user) throws Exception {
		log.debug("REST request to save User : {}", user);
		if (user.getId() != null) {
			throw new Exception("A new user cannot already have an ID");
		}
		User result = userService.save(user);
		log.debug("Added new user:: " + user);
		return new ResponseEntity<User>(result, HttpStatus.CREATED);
	}

	/**
	 * PUT /user : Updates an existing user.
	 *
	 * @param user
	 *            the user to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         user, or exception with status 500 (Internal Server Error) invalid ID, ID is null.
	 * @throws Exception
	 */
	@PutMapping("/user")
	public ResponseEntity<User> updateUser(@RequestBody User user) throws Exception {
		log.debug("REST request to update User : {}", user);

		if (user.getId() == null) {
			throw new Exception("Invalid ID, ID is null");
		}

		User result = userService.save(user);
		return new ResponseEntity<User>(result, HttpStatus.OK);
	}

}
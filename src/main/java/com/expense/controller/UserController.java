package com.expense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expense.entity.User;
import com.expense.entity.UserModel;
import com.expense.exception.ResourceNotFoundException;
import com.expense.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/profile")
	public ResponseEntity<User> getUserDetails() {
		return new ResponseEntity<>(userService.getUserDetail(), HttpStatus.OK);
	}

	@PutMapping("/profile")
	public ResponseEntity<User> updateUserDetails(@RequestBody UserModel user) {
		User updatedUser = userService.updateUserDetails(user);
		return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
	}

	@DeleteMapping("/deactivate")
	public ResponseEntity<String> removeUserDetails() throws ResourceNotFoundException {
		userService.deleteUserDetails();
		return new ResponseEntity<String>("Deleted Successfully!!", HttpStatus.OK);
	}

}

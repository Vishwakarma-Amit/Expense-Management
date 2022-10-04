package com.expense.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expense.entity.AuthModel;
import com.expense.entity.JwtResponse;
import com.expense.entity.User;
import com.expense.entity.UserModel;
import com.expense.security.CustomUserDetailsService;
import com.expense.service.UserService;
import com.expense.utils.JwtTokenUtil;

@RestController
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody AuthModel authModel) throws Exception {
		authenticate(authModel.getEmail(), authModel.getPassword());
		final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authModel.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return new ResponseEntity<JwtResponse>(new JwtResponse(token), HttpStatus.OK);
	}

	private void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (DisabledException de) {
			throw new Exception("User is disabled");
		} catch (BadCredentialsException be) {
			throw new Exception("Invalid credentials!!");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@Valid @RequestBody UserModel user) {
		return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
	}
}
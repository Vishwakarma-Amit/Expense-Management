package com.expense.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expense.entity.User;
import com.expense.entity.UserModel;
import com.expense.exception.ItemAlreadyExistsException;
import com.expense.exception.ResourceNotFoundException;
import com.expense.repository.UserRepository;
import com.expense.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Override
	public User createUser(UserModel userModel) {
		if (userRepository.existsByEmail(userModel.getEmail())) {
			throw new ItemAlreadyExistsException("User is already registered with mail id " + userModel.getEmail());
		}
		User user = new User();
		BeanUtils.copyProperties(userModel, user);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User getUserDetail() {
		Long userId = getLoggedInUser().getId();
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
	}

	@Override
	public User updateUserDetails(UserModel user) {
		User updatedUser = getUserDetail();
		updatedUser.setName(user.getName() != null ? user.getName() : updatedUser.getName());
		updatedUser.setAge(user.getAge() != null ? user.getAge() : updatedUser.getAge());
		updatedUser.setEmail(user.getEmail() != null ? user.getEmail() : updatedUser.getEmail());
		updatedUser.setPassword(user.getPassword() != null ? bCryptPasswordEncoder.encode(user.getPassword())
				: updatedUser.getPassword());
		return userRepository.save(updatedUser);
	}

	@Override
	public void deleteUserDetails() {
		User user = getUserDetail();
		userRepository.delete(user);
	}

	@Override
	public User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found for the email " + email));
	}

}

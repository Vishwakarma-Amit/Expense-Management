package com.expense.service;

import com.expense.entity.User;
import com.expense.entity.UserModel;

public interface UserService {

	User createUser(UserModel userModel);

	User getUserDetail();

	User updateUserDetails(UserModel user);

	void deleteUserDetails();

	User getLoggedInUser();

}

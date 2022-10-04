package com.expense.entity;

import java.util.Date;

import lombok.Data;

@Data
public class CustomException {

	private int statusCode;
	private String message;
	private Date timestamp;

}

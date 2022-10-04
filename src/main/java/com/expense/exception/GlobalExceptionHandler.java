package com.expense.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.expense.entity.CustomException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CustomException> handleExpenseNotFoundException(ResourceNotFoundException ex,
			WebRequest request) {
		CustomException exception = new CustomException();

		exception.setStatusCode(HttpStatus.NOT_FOUND.value());
		exception.setMessage(ex.getMessage());
		exception.setTimestamp(new Date(System.currentTimeMillis()));

		return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentMismatchException(MethodArgumentTypeMismatchException mx,
			WebRequest request) {
		CustomException exception = new CustomException();

		exception.setStatusCode(HttpStatus.BAD_REQUEST.value());
		exception.setMessage(mx.getMessage());
		exception.setTimestamp(new Date(System.currentTimeMillis()));

		return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
		CustomException exception = new CustomException();

		exception.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		exception.setMessage(ex.getMessage());
		exception.setTimestamp(new Date(System.currentTimeMillis()));

		return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ItemAlreadyExistsException.class)
	public ResponseEntity<CustomException> handleItemAlreadyExistsException(ItemAlreadyExistsException ix,
			WebRequest request) {
		CustomException exception = new CustomException();

		exception.setStatusCode(HttpStatus.CONFLICT.value());
		exception.setMessage(ix.getMessage());
		exception.setTimestamp(new Date(System.currentTimeMillis()));

		return new ResponseEntity<>(exception, HttpStatus.CONFLICT);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> body = new HashMap<String, Object>();

		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("timestamp", new Date(System.currentTimeMillis()));
		body.put("statusCode", HttpStatus.BAD_REQUEST);
		body.put("message", errors);

		return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
	}
}

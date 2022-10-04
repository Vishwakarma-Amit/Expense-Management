package com.expense.controller;

import java.sql.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.expense.entity.Expense;
import com.expense.exception.ResourceNotFoundException;
import com.expense.service.ExpenseService;

@RestController
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;

	@GetMapping("/expenses")
	public Page<Expense> getAllExpenses(Pageable page) {
		return expenseService.getAllExpenses(page);
	}

	@GetMapping("/expenses/{id}")
	public Expense getExpenseById(@PathVariable Long id) {
		return expenseService.getExpenseById(id);
	}

	@GetMapping("/expenses/category")
	public List<Expense> getAllExpensesByCategory(@RequestParam String category, Pageable page) {
		return expenseService.readByCategory(category, page);
	}

	@GetMapping("/expenses/name")
	public List<Expense> getAllExpensesByName(@RequestParam String name, Pageable page) {
		return expenseService.readExpenseByName(name, page);
	}

	@GetMapping("/expenses/date")
	public List<Expense> getAllExpensesByDate(@RequestParam(required = false) Date startDate,
			@RequestParam(required = false) Date endDate, Pageable page) {
		return expenseService.readExpenseByDate(startDate, endDate, page);
	}

	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping("/expenses")
	public Expense saveExpenseDetails(@Valid @RequestBody Expense expense) {
		return expenseService.saveExpenseDetails(expense);
	}

	@PutMapping("/expenses/{id}")
	public Expense updateExpenseDetails(@PathVariable Long id, @RequestBody Expense expense) {
		return expenseService.updateExpenseDetails(id, expense);
	}

	@DeleteMapping("/expenses/{id}")
	public ResponseEntity<String> deleteExpenseById(@PathVariable Long id) throws ResourceNotFoundException {
		expenseService.deleteExpenseById(id);
		return new ResponseEntity<String>("Deleted Successfully!!", HttpStatus.OK);
	}

}

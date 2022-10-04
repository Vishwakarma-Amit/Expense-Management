package com.expense.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.expense.exception.ResourceNotFoundException;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.expense.entity.Expense;
import com.expense.repository.ExpenseRepository;
import com.expense.service.ExpenseService;
import com.expense.service.UserService;

@Service

public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepo;

	@Autowired
	private UserService userService;

	@Override
	public Page<Expense> getAllExpenses(Pageable page) {
		return expenseRepo.findByUserId(userService.getLoggedInUser().getId(), page);
	}

	@Override
	public Expense getExpenseById(Long id) {
		Optional<Expense> expense = expenseRepo.findByUserIdAndId(userService.getLoggedInUser().getId(), id);
		if (expense.isPresent()) {
			return expense.get();
		}
		throw new ResourceNotFoundException("Expense is not found for the id " + id);
	}

	@Override
	public void deleteExpenseById(Long id) {
		Expense expense = getExpenseById(id);
		expenseRepo.delete(expense);
	}

	@Override
	public Expense saveExpenseDetails(Expense expense) {
		expense.setUser(userService.getLoggedInUser());
		return expenseRepo.save(expense);
	}

	@Override
	public Expense updateExpenseDetails(Long id, Expense expense) {
		Expense existingExpense = getExpenseById(id);
		existingExpense.setAmount(expense.getAmount() != null ? expense.getAmount() : existingExpense.getAmount());
		existingExpense
				.setCategory(expense.getCategory() != null ? expense.getCategory() : existingExpense.getCategory());
		existingExpense.setDate(expense.getDate() != null ? expense.getDate() : existingExpense.getDate());
		existingExpense.setDescription(
				expense.getDescription() != null ? expense.getDescription() : existingExpense.getDescription());
		existingExpense.setName(expense.getName() != null ? expense.getName() : existingExpense.getName());
		return expenseRepo.save(existingExpense);
	}

	@Override
	public List<Expense> readByCategory(String category, Pageable page) {
		return expenseRepo.findByUserIdAndCategory(userService.getLoggedInUser().getId(), category, page).toList();
	}

	@Override
	public List<Expense> readExpenseByName(String name, Pageable page) {
		return expenseRepo.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(), name, page).toList();
	}

	@Override
	public List<Expense> readExpenseByDate(Date startDate, Date endDate, Pageable page) {
		if (startDate == null) {
			startDate = new Date(0);
		}
		if (endDate == null) {
			endDate = new Date(System.currentTimeMillis());
		}
		Page<Expense> pages = expenseRepo.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(), startDate,
				endDate, page);
		return pages.toList();
	}

}

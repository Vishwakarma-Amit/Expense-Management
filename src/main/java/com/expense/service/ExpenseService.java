package com.expense.service;

import com.expense.entity.Expense;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseService {

	Page<Expense> getAllExpenses(Pageable page);

	List<Expense> readByCategory(String category, Pageable page);

	List<Expense> readExpenseByName(String name, Pageable page);

	List<Expense> readExpenseByDate(Date startDate, Date endDate, Pageable page);

	Expense getExpenseById(Long id);

	void deleteExpenseById(Long id);

	Expense saveExpenseDetails(Expense expense);

	Expense updateExpenseDetails(Long id, Expense expense);
}

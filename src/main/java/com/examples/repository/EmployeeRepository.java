package com.examples.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.examples.model.Employee;

public class EmployeeRepository {

	public static final EmployeeRepository instance = new EmployeeRepository();
	private List<Employee> employees = new LinkedList<Employee>();

	private EmployeeRepository() {
		employees.add(new Employee("ID1", "Peter", 1000));
		employees.add(new Employee("ID2", "Paul", 2000));
		employees.add(new Employee("ID3", "Mary", 3000));
	}

	/**
	 * @return the employees
	 */
	public List<Employee> findAll() {
		return employees;
	}

	public Optional<Employee> findOne(String id) {
		return employees.stream()
			.filter(e -> e.getEmployeeId().equals(id))
			.findFirst();
	}
}

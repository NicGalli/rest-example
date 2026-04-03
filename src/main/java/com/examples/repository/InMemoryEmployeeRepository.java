package com.examples.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.examples.model.Employee;

import jakarta.inject.Inject;

public class InMemoryEmployeeRepository implements EmployeeRepository {

	private Map<String, Employee> employees;

	@Inject
	public InMemoryEmployeeRepository(Map<String, Employee> employees) {
		this.employees = employees;
		put(new Employee("ID1", "Peter", 1000));
		put(new Employee("ID2", "Paul", 2000));
		put(new Employee("ID3", "Mary", 3000));
	}

	private void put(Employee employee) {
		employees.put(employee.getEmployeeId(), employee);
	}

	@Override
	public List<Employee> findAll() {
		return new ArrayList<>(employees.values());
	}

	@Override
	public Optional<Employee> findOne(String id) {
		return Optional.ofNullable(employees.get(id));
	}

	@Override
	public Employee save(Employee employee) {
		if (employee.getEmployeeId() == null) {
			employee.setEmployeeId("ID" + employees.size() + 1);
		}
		put(employee);
		return employee;
	}

	@Override
	public Employee deleteById(String id) {
		return employees.remove(id);
	}
}

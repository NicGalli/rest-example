package com.examples.service;

import java.util.List;
import java.util.Optional;

import com.examples.model.Employee;
import com.examples.repository.EmployeeRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;

	@Inject
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public synchronized List<Employee> allEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public synchronized Employee getEmployeeById(String id) {
		return employeeRepository.findOne(id)
			.orElseThrow(() -> new NotFoundException(
				"Employee with id " + id + " not found"));
	}

	@Override
	public synchronized Employee addEmployee(Employee employee) {
		sanityChecks(employee);
		return employeeRepository.save(employee);
	}

	private void sanityChecks(Employee employee) {
		if (employee == null) {
			throw new BadRequestException();
		}
		if (employee.getEmployeeId() != null) {
			throw new BadRequestException();
		}
	}

	@Override
	public synchronized Employee replaceEmployeeById(String id, Employee employee) {
		sanityChecks(employee);
		if (!employeeRepository.findOne(id).isPresent()) {
			throw new NotFoundException();
		}
		employee.setEmployeeId(id);
		return employeeRepository.save(employee);
	}

	@Override
	public synchronized Employee deleteEmployeeById(String id) {
		return employeeRepository.deleteById(id);	}
}

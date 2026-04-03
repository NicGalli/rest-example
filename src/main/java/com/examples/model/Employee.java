package com.examples.model;

import java.util.Objects;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee {

	private String employeeId;
	private String name;
	private int salary;

	public Employee() {}

	public Employee(String employeeId, String name, int salary) {
		this.employeeId = employeeId;
		this.name = name;
		this.salary = salary;
	}

	
	/**
	 * @return the employeeId
	 */
	@XmlElement(name = "id")
	public String getEmployeeId() {
		return employeeId;
	}

	
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * @return the salary
	 */
	public int getSalary() {
		return salary;
	}

	
	/**
	 * @param salary the salary to set
	 */
	public void setSalary(int salary) {
		this.salary = salary;
	}

	@Override
	public int hashCode() {
		return Objects.hash(employeeId, name, salary);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(employeeId, other.employeeId)
			&& Objects.equals(name, other.name) && salary == other.salary;
	}
	

}

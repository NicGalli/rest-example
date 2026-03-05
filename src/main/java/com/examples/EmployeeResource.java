package com.examples;

import java.util.List;

import com.examples.model.Employee;
import com.examples.repository.EmployeeRepository;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("employees")
public class EmployeeResource {

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Employee> getAllEmployees() {
		return EmployeeRepository.instance.findAll();
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Employee getOneEmployee(@PathParam("id") String id) {
		return EmployeeRepository.instance.findOne(id)
			.orElseThrow(() -> new NotFoundException(
				"Employee with id " + id + " not found"));
	}
}

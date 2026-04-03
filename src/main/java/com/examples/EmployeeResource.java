package com.examples;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.examples.model.Employee;
import com.examples.service.EmployeeService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("employees")
public class EmployeeResource {

	@Inject
	EmployeeService employeeService;

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Employee> getAllEmployees() {
		return employeeService.allEmployees();
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Employee getOneEmployee(@PathParam("id") String id) {
		return employeeService.getEmployeeById(id);
	}

	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		return String.valueOf(employeeService.allEmployees().size());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEmployee(Employee employee, @Context UriInfo uriInfo)
		throws URISyntaxException {
		Employee saved = employeeService.addEmployee(employee);
		return Response
			.created(new URI(
				uriInfo.getAbsolutePath() + "/" + saved.getEmployeeId()))
			.entity(saved)
			.build();
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Employee replaceEmployee(@PathParam("id") String id,
		Employee employee) {
		return employeeService.replaceEmployeeById(id, employee);
	}

	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deletEmployee(@PathParam("id") String id) {
		return Response.accepted(employeeService.deleteEmployeeById(id))
			.build();
	}
}

package com.examples;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.List;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.examples.model.Employee;
import com.examples.service.EmployeeService;

import io.restassured.RestAssured;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;

@ExtendWith(MockitoExtension.class)
class EmployeeResourceRestAssuredTest extends JerseyTest {

	private static final String EMPLOYEES = "employees";
	@Mock
	private EmployeeService employeeService;

	@Override
	protected Application configure() {
		return new ResourceConfig(EmployeeResource.class)
			.register(new AbstractBinder() {

				@Override
				protected void configure() {
					bind(employeeService).to(EmployeeService.class);
				}
			});
	}

	@BeforeEach
	public void configureRestAssured() {
		RestAssured.baseURI = getBaseUri().toString();
	}

	@Test
	void testGetAllEmployees() {
		when(employeeService.allEmployees())
			.thenReturn(List.of(new Employee("ID1", "Peter", 1000),
				new Employee("ID2", "Paul", 2000),
				new Employee("ID3", "Mary", 3000)));
		given().accept(MediaType.APPLICATION_XML)
			.when()
			.get(EMPLOYEES)
			.then()
			.statusCode(200)
			.assertThat()
			.body("employees.employee[0].id", equalTo("ID1"),
				"employees.employee[0].name", equalTo("Peter"),
				"employees.employee[0].salary", equalTo("1000"),
				"employees.employee[1].id", equalTo("ID2"),
				"employees.employee[1].name", equalTo("Paul"),
				"employees.employee[1].salary", equalTo("2000"),
				"employees.employee[2].id", equalTo("ID3"),
				"employees.employee[2].name", equalTo("Mary"),
				"employees.employee[2].salary", equalTo("3000"));
	}

	@Test
	void testGetAllEmployeesWithRootPaths() {
		when(employeeService.allEmployees())
			.thenReturn(List.of(new Employee("ID1", "Peter", 1000),
				new Employee("ID2", "Paul", 2000),
				new Employee("ID3", "Mary", 3000)));
		given().accept(MediaType.APPLICATION_XML)
			.when()
			.get(EMPLOYEES)
			.then()
			.statusCode(200)
			.assertThat()
			.rootPath("employees.employee[0]")
			.body("id", equalTo("ID1"), "name", equalTo("Peter"), "salary",
				equalTo("1000"))
			.rootPath("employees.employee[1]")
			.body("id", equalTo("ID2"), "name", equalTo("Paul"), "salary",
				equalTo("2000"))
			.rootPath("employees.employee[2]")
			.body("id", equalTo("ID3"), "name", equalTo("Mary"), "salary",
				equalTo("3000"));
	}

	@Test
	void testGetOneEmployee() {
		when(employeeService.getEmployeeById(Mockito.anyString()))
			.thenReturn(new Employee("ID2", "Paul", 2000));
		given().accept(MediaType.APPLICATION_XML)
			.when()
			.get(EMPLOYEES + "/ID2")
			.then()
			.statusCode(200)
			.assertThat()
			.body("employee.id", equalTo("ID2"), "employee.name",
				equalTo("Paul"), "employee.salary", equalTo("2000"));
	}

	@Test
	void testGetOneEmployeeJSON() {
		when(employeeService.getEmployeeById("ID2"))
			.thenReturn(new Employee("ID2", "Paul", 2000));
		given().accept(MediaType.APPLICATION_JSON)
			.when()
			.get(EMPLOYEES + "/ID2")
			.then()
			.statusCode(200)
			.assertThat()
			.body("id", equalTo("ID2"), "name", equalTo("Paul"), "salary",
				equalTo(2000));
	}

	@Test
	void testGetAllEmployeesJSON() {
		when(employeeService.allEmployees())
			.thenReturn(List.of(new Employee("ID1", "Peter", 1000),
				new Employee("ID2", "Paul", 2000),
				new Employee("ID3", "Mary", 3000)));
		given().accept(MediaType.APPLICATION_JSON)
			.when()
			.get(EMPLOYEES)
			.then()
			.statusCode(200)
			.assertThat()
			.body("id[0]", equalTo("ID1"), "name[0]", equalTo("Peter"),
				"salary[0]", equalTo(1000), "id[1]", equalTo("ID2"), "name[1]",
				equalTo("Paul"), "salary[1]", equalTo(2000));
	}

	@Test
	void justForDemoCantAccessAlsoMyResource() {
		given().accept(MediaType.TEXT_PLAIN)
			.when()
			.get("myresource")
			.then()
			.statusCode(404);
	}

	@Test
	void testCount() {
		List<Employee> employees = asList(new Employee(), new Employee());
		when(employeeService.allEmployees()).thenReturn(employees);
		given().accept(MediaType.TEXT_PLAIN)
			.when()
			.get(EMPLOYEES + "/count")
			.then()
			.statusCode(200)
			.and()
			.body(equalTo("" + employees.size()));
	}

	@Test
	void testPost() {
		JsonObject newObject = Json.createObjectBuilder()
			.add("name", "John")
			.add("salary", 4000)
			.build();
		when(employeeService.addEmployee(new Employee(null, "John", 4000)))
			.thenReturn(new Employee("ID", "boh", 1200));
		given().contentType(MediaType.APPLICATION_JSON)
			.body(newObject.toString())
			.when()
			.post(EMPLOYEES)
			.then()
			.statusCode(201)
			.assertThat()
			.body("id", equalTo("ID"), "name", equalTo("boh"), "salary",
				equalTo(1200))
			.header("Location", response -> endsWith(EMPLOYEES + "/ID"));
	}

	@Test
	void testPutEmployee() {
		JsonObject newObject = Json.createObjectBuilder()
			.add("name", "passed name")
			.add("salary", 1000)
			.build();
		when(employeeService.replaceEmployeeById("ID",
			new Employee(null, "passed name", 1000)))
				.thenReturn(new Employee("ID", "returned name", 2000));
		given().contentType(MediaType.APPLICATION_JSON)
			.body(newObject.toString())
			.when()
			.put(EMPLOYEES + "/ID")
			.then()
			.statusCode(200)
			.assertThat()
			.body("id", equalTo("ID"), "name", equalTo("returned name"),
				"salary", equalTo(2000));
	}

	@Test
	void testDeleteEmployee() {
		when(employeeService.deleteEmployeeById("ID1"))
			.thenReturn(new Employee("ID1", "deleted name", 2000))
			.thenReturn(null);
		given().accept(MediaType.APPLICATION_JSON)
			.when()
			.delete(EMPLOYEES + "/ID1")
			.then()
			.statusCode(202)
			.assertThat()
			.body("id", equalTo("ID1"), "name", equalTo("deleted name"),
				"salary", equalTo(2000));
		given().accept(MediaType.APPLICATION_JSON)
			.when()
			.delete(EMPLOYEES + "/ID1")
			.then()
			.statusCode(202)
			.assertThat()
			.body(is(emptyString()));
	}
}

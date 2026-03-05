package com.examples;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import jakarta.ws.rs.core.MediaType;

class EmployeeResourceRestAssuredTest {

	private HttpServer server;
	private static final String EMPLOYEES = "employees";

	@BeforeAll
	static void setUpBeforeClass() {
		RestAssured.baseURI = Main.BASE_URI;
	}

	@BeforeEach
	void setUp() {
		server = Main.startServer();
	}

	@AfterEach
	void tearDown() {
		server.shutdownNow();
	}

	@Test
	void testGetAllEmployees() {
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
		given().accept(MediaType.APPLICATION_JSON)
			.when()
			.get(EMPLOYEES + "/ID2")
			.then()
			.statusCode(200)
			.assertThat()
			.body("id", equalTo("ID2"), "name",
				equalTo("Paul"), "salary", equalTo(2000));
	}

	@Test
	void testGetOneEmployeeWithNonExistingId() {
		given().accept(MediaType.APPLICATION_XML)
			.when()
			.get(EMPLOYEES + "/ID24")
			.then()
			.statusCode(404)
			.contentType(MediaType.TEXT_PLAIN)
			.body(equalTo("Employee with id ID24 not found"));
	}
	
	@Test
	void testGetOneEmployeeWithNonExistingIdJSON() {
		given().accept(MediaType.APPLICATION_JSON)
			.when()
			.get(EMPLOYEES + "/ID24")
			.then()
			.statusCode(404)
			.contentType(MediaType.TEXT_PLAIN)
			.body(equalTo("Employee with id ID24 not found"));
	}

	@Test
	void testGetAllEmployeesJSON() {
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
}

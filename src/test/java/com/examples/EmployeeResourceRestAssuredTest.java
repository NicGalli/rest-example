/**
 * 
 */
package com.examples;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import jakarta.ws.rs.core.MediaType;

/**
 * 
 */
class EmployeeResourceRestAssuredTest {

	private HttpServer server;

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
	void testGetItXML() {
		given().accept(MediaType.APPLICATION_XML)
			.when()
			.get("employee")
			.then()
			.statusCode(200)
			.assertThat()
			.body("employee.id", equalTo("E1"), "employee.name",
				equalTo("John"), "employee.salary", equalTo("1000"));
	}
}

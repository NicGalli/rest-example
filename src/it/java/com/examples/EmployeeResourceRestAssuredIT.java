package com.examples;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.examples.model.Employee;

import io.restassured.RestAssured;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;

class EmployeeResourceRestAssuredIT {

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

//	@Test
//	void testGetAllEmployees() {
//		given().accept(MediaType.APPLICATION_XML)
//			.when()
//			.get(EMPLOYEES)
//			.then()
//			.statusCode(200)
//			.assertThat()
//			.body("employees.employee[0].id", equalTo("ID1"),
//				"employees.employee[0].name", equalTo("Peter"),
//				"employees.employee[0].salary", equalTo("1000"),
//				"employees.employee[1].id", equalTo("ID2"),
//				"employees.employee[1].name", equalTo("Paul"),
//				"employees.employee[1].salary", equalTo("2000"),
//				"employees.employee[2].id", equalTo("ID3"),
//				"employees.employee[2].name", equalTo("Mary"),
//				"employees.employee[2].salary", equalTo("3000"));
//	}
//
//	@Test
//	void testGetAllEmployeesWithRootPaths() {
//		given().accept(MediaType.APPLICATION_XML)
//			.when()
//			.get(EMPLOYEES)
//			.then()
//			.statusCode(200)
//			.assertThat()
//			.rootPath("employees.employee[0]")
//			.body("id", equalTo("ID1"), "name", equalTo("Peter"), "salary",
//				equalTo("1000"))
//			.rootPath("employees.employee[1]")
//			.body("id", equalTo("ID2"), "name", equalTo("Paul"), "salary",
//				equalTo("2000"))
//			.rootPath("employees.employee[2]")
//			.body("id", equalTo("ID3"), "name", equalTo("Mary"), "salary",
//				equalTo("3000"));
//	}
//
//	@Test
//	void testGetOneEmployee() {
//		given().accept(MediaType.APPLICATION_XML)
//			.when()
//			.get(EMPLOYEES + "/ID2")
//			.then()
//			.statusCode(200)
//			.assertThat()
//			.body("employee.id", equalTo("ID2"), "employee.name",
//				equalTo("Paul"), "employee.salary", equalTo("2000"));
//	}
//
//	@Test
//	void testGetOneEmployeeJSON() {
//		given().accept(MediaType.APPLICATION_JSON)
//			.when()
//			.get(EMPLOYEES + "/ID2")
//			.then()
//			.statusCode(200)
//			.assertThat()
//			.body("id", equalTo("ID2"), "name", equalTo("Paul"), "salary",
//				equalTo(2000));
//	}

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

//	@Test
//	void testGetAllEmployeesJSON() {
//		given().accept(MediaType.APPLICATION_JSON)
//			.when()
//			.get(EMPLOYEES)
//			.then()
//			.statusCode(200)
//			.assertThat()
//			.body("id[0]", equalTo("ID1"), "name[0]", equalTo("Peter"),
//				"salary[0]", equalTo(1000), "id[1]", equalTo("ID2"), "name[1]",
//				equalTo("Paul"), "salary[1]", equalTo(2000));
//	}

	@Test
	void justForDemoCanAccessAlsoMyResource() {
		given().accept(MediaType.TEXT_PLAIN)
			.when()
			.get("myresource")
			.then()
			.statusCode(200)
			.assertThat()
			.contentType(MediaType.TEXT_PLAIN)
			.and()
			.body(equalTo("Got it!"));
	}

	@Test
	void testPost() {
		JsonObject newObject = Json.createObjectBuilder()
			.add("name", "John")
			.add("salary", 4000)
			.build();
		io.restassured.response.Response response = given()
			.contentType(MediaType.APPLICATION_JSON)
			.body(newObject.toString())
			.when()
			.post(EMPLOYEES);
		String id = response.body().path("id");
		String uri = response.header("Location");
		System.out.println(uri);
		given().accept(MediaType.APPLICATION_JSON)
			.when()
			.get(uri)
			.then()
			.statusCode(200)
			.assertThat()
			.body("id", equalTo(id), "name", equalTo("John"), "salary",
				equalTo(4000));
	}

//	@Test
//	void testMultiplePost() {
//		JsonObject[] objects = new JsonObject[3];
//		objects[0] = Json.createObjectBuilder()
//			.add("name", "Will")
//			.add("salary", 5000)
//			.build();
//		objects[1] = Json.createObjectBuilder()
//			.add("name", "Mike")
//			.add("salary", 6000)
//			.build();
//		objects[2] = Json.createObjectBuilder()
//			.add("name", "John")
//			.add("salary", 4000)
//			.build();
//		io.restassured.response.Response[] responses = new io.restassured.response.Response[3];
//		for (int i = 0; i < responses.length; i++) {
//			responses[i] = given().contentType(MediaType.APPLICATION_JSON)
//				.body(objects[i].toString())
//				.when()
//				.post(EMPLOYEES);
//		}
//		String id1 = responses[0].body().path("id");
//		String uri1 = responses[0].header("Location");
//		given().accept(MediaType.APPLICATION_JSON)
//			.when()
//			.get(uri1)
//			.then()
//			.statusCode(200)
//			.assertThat()
//			.body("id", equalTo(id1), "name", equalTo("Will"), "salary",
//				equalTo(5000));
//		String id2 = responses[1].body().path("id");
//		String uri2 = responses[1].header("Location");
//		System.out.println(uri2);
//		given().accept(MediaType.APPLICATION_JSON)
//			.when()
//			.get(uri2)
//			.then()
//			.statusCode(200)
//			.assertThat()
//			.body("id", equalTo(id2), "name", equalTo("Mike"), "salary",
//				equalTo(6000));
//		String id3 = responses[2].body().path("id");
//		String uri3 = responses[2].header("Location");
//		System.out.println(uri3);
//		given().accept(MediaType.APPLICATION_JSON)
//			.when()
//			.get(uri3)
//			.then()
//			.statusCode(200)
//			.assertThat()
//			.body("id", equalTo(id3), "name", equalTo("John"), "salary",
//				equalTo(4000));
//		org.junit.jupiter.api.Assertions.assertNotEquals(id1, id2);
//		org.junit.jupiter.api.Assertions.assertNotEquals(id2, id3);
//		org.junit.jupiter.api.Assertions.assertNotEquals(id1, id3);
//	}

	@Test
	void testPostNewEmployeeConcurrent() {
		JsonObject newObject = Json.createObjectBuilder()
			.add("name", "John")
			.add("salary", 4000)
			.build();
		Collection<String> ids = new ConcurrentLinkedQueue<>();
		List<Thread> threads = IntStream.range(0, 10)
			.mapToObj(i -> new Thread(() -> {
				io.restassured.response.Response response = given()
					.contentType(MediaType.APPLICATION_JSON)
					.body(newObject.toString())
					.when()
					.post(EMPLOYEES);
				ids.add(response.path("id"));
			}))
			.peek(t -> t.start())
			.collect(Collectors.toList());
		await().atMost(10, SECONDS)
			.until(() -> threads.stream().noneMatch(t -> t.isAlive()));
		assertThat(ids).doesNotHaveDuplicates();
	}

	@Test
	void testPutEmployee() {
		JsonObject newObject = Json.createObjectBuilder()
			.add("name", "passed name")
			.add("salary", 2000)
			.build();
		given().contentType(MediaType.APPLICATION_JSON)
			.body(newObject.toString())
			.when()
			.put(EMPLOYEES + "/ID1")
			.then()
			.statusCode(200)
			.assertThat()
			.body("id", equalTo("ID1"), "name", equalTo("passed name"),
				"salary", equalTo(2000));
		given().accept(MediaType.APPLICATION_JSON)
			.when()
			.get(EMPLOYEES + "/ID1")
			.then()
			.statusCode(200)
			.assertThat()
			.body("id", equalTo("ID1"), "name", equalTo("passed name"),
				"salary", equalTo(2000));
	}

	@Test
	void testPutBadRequestWhenIdIdPartOfTheBody() {
		JsonObject newObject = Json.createObjectBuilder()
			.add("id", "ID3")
			.add("name", "passed name")
			.add("salary", 2000)
			.build();
		given().contentType(MediaType.APPLICATION_JSON)
			.body(newObject.toString())
			.when()
			.put(EMPLOYEES + "/ID1")
			.then()
			.statusCode(400)
			.assertThat();
	}
	
	@Test
	void testDeleteEmployee() {
		
		given().accept(MediaType.APPLICATION_JSON)
			.when()
			.delete(EMPLOYEES + "/ID1")
			.then()
			.statusCode(202)
			.assertThat()
			.body("id", equalTo("ID1"), "name", equalTo("Peter"),
				"salary", equalTo(1000));
		given().accept(MediaType.APPLICATION_JSON)
			.when()
			.delete(EMPLOYEES + "/ID1")
			.then()
			.statusCode(202)
			.assertThat()
			.body(is(emptyString()));
		
		given().accept(MediaType.APPLICATION_XML)
		.when()
		.get(EMPLOYEES + "/ID1")
		.then()
		.statusCode(404)
		.contentType(MediaType.TEXT_PLAIN)
		.body(equalTo("Employee with id ID1 not found"));
	}
}

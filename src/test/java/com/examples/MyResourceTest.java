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

public class MyResourceTest {

	private HttpServer server;
//	private WebTarget target;

	@BeforeAll
	static void configureRestAssured() {
		RestAssured.baseURI = Main.BASE_URI;
	}

	@BeforeEach
	public void setUp() throws Exception {
		// start the server
		server = Main.startServer();
		// create the client
//		Client c = ClientBuilder.newClient();

		// uncomment the following line if you want to enable
		// support for JSON in the client (you also have to uncomment
		// dependency on jersey-media-json module in pom.xml and
		// Main.startServer())
		// --
		// c.configuration().enable(new
		// org.glassfish.jersey.media.json.JsonJaxbFeature());

//		target = c.target(Main.BASE_URI);
	}

	@AfterEach
	public void tearDown() throws Exception {
		server.shutdownNow();
	}

	/**
	 * Test to see that the message "Got it!" is sent in the response.
	 */
	@Test
	public void testGetItText() {
//		String responseMsg = target.path("myresource").request(TEXT_PLAIN)
//			.get(String.class);
//		assertEquals("Got it!", responseMsg);

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
	public void testGetItHTML() {
//		String responseMsg = target.path("myresource").request(MediaType.TEXT_HTML)
//			.get(String.class);
//		assertEquals("Got it!", responseMsg);
		given().accept(MediaType.TEXT_HTML)
		.when()
		.get("myresource")
		.then()
		.statusCode(200)
		.assertThat()
		.contentType(MediaType.TEXT_HTML)
		.and()
		.body(equalTo("Got it!"));
	}

	@Test
	public void testGetItXML() {
//		String responseMsg = target.path("myresource").request(MediaType.TEXT_XML)
//			.get(String.class);
//		assertEquals("Got it!", responseMsg);
		given().accept(MediaType.TEXT_XML)
		.when()
		.get("myresource")
		.then()
		.statusCode(200)
		.assertThat()
		.contentType(MediaType.TEXT_XML)
		.and()
		.body(equalTo("Got it!"));
	}
}

package com.examples;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;

class BadRequestMapperTest extends JerseyTest {

	@Path("testpath")
	public static class MockResource {

		@GET
		@Path("badrequest")
		public String testEndPoint() {
			throw new BadRequestException("error message");
		}
	}

	@Override
	protected Application configure() {
		return new ResourceConfig().register(BadRequestMapper.class)
			.register(MockResource.class);
	}

	@BeforeEach
	void configureRestAssured() {
		RestAssured.baseURI = getBaseUri().toString();
	}

	@Test
	void testBadRequestResponse() {
		when().get("testpath/badrequest")
			.then()
			.statusCode(400)
			.contentType(MediaType.TEXT_PLAIN)
			.body(equalTo("error message"));
	}
}

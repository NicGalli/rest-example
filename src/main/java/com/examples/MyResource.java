package com.examples;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getItText() {
        return getProcessor();
    }
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getItHTML() {
        return getProcessor();
    }
    @GET
    @Produces(MediaType.TEXT_XML)
    public String getItXML() {
        return getProcessor();
    }
	private String getProcessor() {
		return "Got it!";
	}
}

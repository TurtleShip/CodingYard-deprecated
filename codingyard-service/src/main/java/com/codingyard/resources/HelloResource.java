package com.codingyard.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("hello")
public class HelloResource {

    @GET
    public String sayHi() {
        return "Wassup, Homie!";
    }
}

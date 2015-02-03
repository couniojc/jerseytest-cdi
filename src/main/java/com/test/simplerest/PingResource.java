package com.test.simplerest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * A very simple resource just to test server is up and running
 * 
 * @author jcounio
 */
@ApplicationScoped
@Path("/ping")
public class PingResource {

    @Inject 
    MyInj inj;

    /**
     * Get the ping string to check if server is up
     */
    @GET
    public String ping() {
        return ""+inj.getString();
    }

}

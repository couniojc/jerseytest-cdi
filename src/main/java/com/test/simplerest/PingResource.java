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

    @Inject
    MyInterface intf;
    
    /**
     * Get the ping string to check if server is up
     */
    @GET
    public String ping() {
        try {
                if (inj == null) {
                    return "inj is null !";
                }                
                return "" + inj.getString();
        } catch (Exception e) {
            //Capture to avoid Jersey to hide everything behind 500 error
            //This is just for the test, use ExceptionMapper in production code !!
            e.printStackTrace();
            return e.toString();
        }
    }
    
    @GET
    @Path("/intf")
    public String pingWithInterface() {
        try {
                if (intf == null) {
                    return "intf is null !";
                }                
                return "" + intf.getString();
        } catch (Exception e) {
            //Capture to avoid Jersey to hide everything behind 500 error
            //This is just for the test, use ExceptionMapper in production code !!
            e.printStackTrace();
            return e.toString();
        }
    }

}

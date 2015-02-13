/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.simplerest;

import com.test.simplerest.jersey.ext.CdiAwareJettyTestContainerFactory;
import javax.ws.rs.core.Application;
import org.junit.Assert;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;

import org.junit.runner.RunWith;
/**
 *
 * @author jcounio
 */
@RunWith(CdiTestRunner.class)
public class PingResourceSimpleTest extends JerseyTest {
    
    static
    {
        System.setProperty("jersey.config.test.container.factory", CdiAwareJettyTestContainerFactory.class.getName());
    }
    
    @Override
    protected Application configure() {
        return new MyApp();
    }
    
    @Test
    public void testPing() {
        //no mock, so it should return normal result
        String resp = target("/ping").request().get(String.class);
        Assert.assertEquals("Ping", "aaa", resp);
    }    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.simplerest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Application;
import junit.framework.Assert;
import org.apache.deltaspike.testcontrol.api.TestControl;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.apache.deltaspike.testcontrol.api.mock.ApplicationMockManager;
import org.apache.deltaspike.testcontrol.api.mock.DynamicMockManager;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.MockType;

import org.glassfish.jersey.test.JerseyTest;
import org.jboss.weld.environment.se.Weld;
import org.junit.Test;

import org.junit.runner.RunWith;
/**
 *
 * @author jcounio
 */
@RunWith(CdiTestRunner.class)
public class PingResourceSimpleTest extends JerseyTest {
    
    @Override
    protected Application configure() {
        return new MyApp();
    }
    
    @Test
    public void testPing() {
        String resp = target("/ping").request().get(String.class);
        Assert.assertEquals("Ping", "aaa", resp);
    }    
}

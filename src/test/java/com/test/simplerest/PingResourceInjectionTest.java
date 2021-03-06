/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.simplerest;

import com.test.simplerest.jersey.ext.CdiAwareJettyTestContainerFactory;
import javax.ws.rs.core.Application;
import org.junit.Assert;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.apache.deltaspike.testcontrol.api.mock.ApplicationMockManager;
import org.easymock.EasyMock;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.runner.RunWith;
/**
 *
 * @author jcounio
 */
@RunWith(CdiTestRunner.class)
public class PingResourceInjectionTest extends JerseyTest {
 
    static
    {
        System.setProperty("jersey.config.test.container.factory", CdiAwareJettyTestContainerFactory.class.getName());
    }
    
    static MyInj injMock;
        
    @Override
    protected Application configure() {
        return new MyApp();
        
    }
    
    @BeforeClass
    public static void init() {
        injMock  = EasyMock.createNiceMock(MyInj.class);
        ApplicationMockManager applicationMockManager = BeanProvider.getContextualReference(ApplicationMockManager.class);
        applicationMockManager.addMock(injMock);        
    }    
    
    @Test
    public void testPingWithNiceMock() {
        String resp = target("/ping").request().get(String.class);
        Assert.assertEquals("Ping", "null", resp);
    }
    
   @Test
    public void testPingWithInstrumentedMock() {
        EasyMock.reset(injMock);
        EasyMock.expect(injMock.getString()).andReturn("bbb");
        EasyMock.replay(injMock);
        String resp = target("/ping").request().get(String.class);
        Assert.assertEquals("Ping", "bbb", resp);
    }

}

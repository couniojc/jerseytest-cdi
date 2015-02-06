/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.simplerest;

import com.test.simplerest.jersey.ext.CdiAwareJettyTestContainerFactory;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.Application;
import junit.framework.Assert;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.apache.deltaspike.testcontrol.api.mock.ApplicationMockManager;
import org.easymock.EasyMock;
import org.easymock.MockType;

import org.glassfish.jersey.test.JerseyTest;
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
    
    @Inject
    ApplicationMockManager applicationMockManager;
    
    @Override
    protected Application configure() {
        return new MyApp();
        
    }
    
   @Test
    public void testPingWithNiceMock() {
        //Since the mock is injected as applicationScope, there's only one instance used.
        //Be sure to work in the same test or pre-initialize the Mock behavior for all your tests
        //Doing this in different tests/threads will fail
        MyInj injMock = EasyMock.createMock(MockType.NICE, MyInj.class);
        applicationMockManager.addMock(injMock);
        String resp = target("/ping").request().get(String.class);
        Assert.assertEquals("Ping", "null", resp);
        //Reset mock for second call
        EasyMock.reset(injMock);
        EasyMock.expect(injMock.getString()).andReturn("bbb");
        EasyMock.replay(injMock);
        resp = target("/ping").request().get(String.class);
        Assert.assertEquals("Ping", "bbb", resp);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.simplerest;

import com.test.simplerest.jersey.ext.CdiAwareJettyTestContainerFactory;
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
        //the nice mock will return null
        MyInj injMock = EasyMock.createMock(MockType.NICE, MyInj.class);
        applicationMockManager.addMock(injMock);
        String resp = target("/ping").request().get(String.class);
        Assert.assertEquals("Ping", "null", resp);
    }
    
    @Test
    public void testPingWithInstrumentedMock() {
        MyInj injMock = EasyMock.createMock(MyInj.class);
        applicationMockManager.addMock(injMock);
        EasyMock.reset(injMock);
        EasyMock.expect(injMock.getString()).andReturn("bbb");
        EasyMock.replay(injMock);
        String resp = target("/ping").request().get(String.class);
        Assert.assertEquals("Ping", "bbb", resp);
    }

}

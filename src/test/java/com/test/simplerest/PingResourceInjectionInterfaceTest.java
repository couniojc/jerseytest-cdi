/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.simplerest;

import com.test.simplerest.jersey.ext.CdiAwareJettyTestContainerFactory;
import javax.enterprise.inject.Typed;
import javax.ws.rs.client.Client;
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
public class PingResourceInjectionInterfaceTest extends JerseyTest {
 
    static
    {
        System.setProperty("jersey.config.test.container.factory", CdiAwareJettyTestContainerFactory.class.getName());
    }
    
    static MyInterface intfMock;
        
    @Override
    protected Application configure() {
        return new MyApp();
        
    }
    
    //You have to Type the Interface, otherwise DeltaSpike will fail to recognize it
    //See https://issues.apache.org/jira/browse/DELTASPIKE-707
    @Typed(MyInterface.class)
    public static abstract class MyInterfaceMock implements MyInterface {
    }
    
    @BeforeClass
    public static void init() {
        //Doesn't work due to DeltaSpike not supporting interfaces
        //intfMock  = EasyMock.createNiceMock(MyInterface.class);
        //Using the Typed class is the only way to make it work
        intfMock  = EasyMock.createNiceMock(MyInterfaceMock.class);        
        ApplicationMockManager applicationMockManager = BeanProvider.getContextualReference(ApplicationMockManager.class);
        applicationMockManager.addMock((MyInterface)intfMock);        
    }    
    
    @Test
    public void testPingWithNiceMock() {
        String resp = target("/ping/intf").request().get(String.class);
        Assert.assertEquals("Ping", "null", resp);
    }
    
   @Test
    public void testPingWithInstrumentedMock() {
        EasyMock.reset(intfMock);
        EasyMock.expect(intfMock.getString()).andReturn("bbb");
        EasyMock.replay(intfMock);
        String resp = target("/ping/intf").request().get(String.class);
        Assert.assertEquals("Ping", "bbb", resp);
    }

}

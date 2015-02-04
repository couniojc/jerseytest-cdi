/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.simplerest.jersey.ext;

import java.net.URI;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerFactory;

/**
 *
 * @author jcounio
 */
public class CdiAwareJettyTestContainerFactory implements TestContainerFactory {
    @Override
    public TestContainer create(final URI baseUri, final DeploymentContext context) throws IllegalArgumentException
    {
        return new CdiAwareJettyTestContainer(baseUri, context);
    }    
}

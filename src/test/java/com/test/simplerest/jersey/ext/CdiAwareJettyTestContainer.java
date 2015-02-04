package com.test.simplerest.jersey.ext;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import javax.ws.rs.core.UriBuilder;
import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestHelper;

public class CdiAwareJettyTestContainer implements TestContainer {

    private static final Logger LOGGER = Logger.getLogger(CdiAwareJettyTestContainer.class.getName());

    private URI baseUri;
    private final Server server;

    private class CdiAwareHandlerWrapper extends HandlerWrapper {

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();

            try {
                cdiContainer.getContextControl().startContext(RequestScoped.class);
                super.handle(target, baseRequest, request, response);
            } finally {
                cdiContainer.getContextControl().stopContext(RequestScoped.class);
            }
        }
    }

    public CdiAwareJettyTestContainer(final URI baseUri, final DeploymentContext context) {
        final URI base = UriBuilder.fromUri(baseUri).path(context.getContextPath()).build();

        if (!"/".equals(base.getRawPath())) {
            throw new TestContainerException(String.format(
                    "Cannot deploy on %s. Jetty HTTP container only supports deployment on root path.",
                    base.getRawPath()));
        }

        this.baseUri = base;

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Creating JettyTestContainer configured at the base URI "
                    + TestHelper.zeroPortToAvailablePort(baseUri));
        }

        this.server = JettyHttpContainerFactory.createServer(this.baseUri, context.getResourceConfig(), false);
        HandlerWrapper cdiHandlerWrapper = new CdiAwareHandlerWrapper();
        cdiHandlerWrapper.setHandler(this.server.getHandler());
        this.server.setHandler(cdiHandlerWrapper);
    }

    @Override
    public ClientConfig getClientConfig() {
        return null;
    }

    @Override
    public URI getBaseUri() {
        return baseUri;
    }

    @Override
    public void start() {
        if (server.isStarted()) {
            LOGGER.log(Level.WARNING, "Ignoring start request - JettyTestContainer is already started.");
        } else {
            LOGGER.log(Level.FINE, "Starting JettyTestContainer...");
            try {
                server.start();

                if (baseUri.getPort() == 0) {
                    int port = 0;
                    for (final Connector connector : server.getConnectors()) {
                        if (connector instanceof ServerConnector) {
                            port = ((ServerConnector) connector).getLocalPort();
                            break;
                        }
                    }

                    baseUri = UriBuilder.fromUri(baseUri).port(port).build();

                    LOGGER.log(Level.INFO, "Started JettyTestContainer at the base URI " + baseUri);
                }
            } catch (Exception e) {
                throw new TestContainerException(e);
            }
        }
    }

    @Override
    public void stop() {
        if (server.isStarted()) {
            LOGGER.log(Level.FINE, "Stopping JettyTestContainer...");
            try {
                this.server.stop();
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Error Stopping JettyTestContainer...", ex);
            }
        } else {
            LOGGER.log(Level.WARNING, "Ignoring stop request - JettyTestContainer is already stopped.");
        }
    }
}

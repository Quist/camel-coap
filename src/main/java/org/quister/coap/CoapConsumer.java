package org.quister.coap;

import org.apache.camel.Processor;
import org.apache.camel.impl.EventDrivenPollingConsumer;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

class CoapConsumer extends EventDrivenPollingConsumer {

    private final Logger logger = LoggerFactory.getLogger(CoapConsumer.class);

    private final CamelCoapEndpoint endpoint;
    private final Processor processor;
    private final int port;

    public CoapConsumer(CamelCoapEndpoint camelCoapEndpoint, int port, Processor processor) {
        super(camelCoapEndpoint);
        this.endpoint = camelCoapEndpoint;
        this.port = port;
        this.processor = processor;
    }

    @Override
    protected void doStart() throws Exception {
        logger.info("Starting COAP server");
        CoapServer server = new CoapServer();
        addEndpoint(server);
        server.add(new CamelCoapResource("test", endpoint, processor));
        server.start();
    }

    @Override
    protected void doStop() throws Exception {

    }

    private void addEndpoint(CoapServer server) {
        InetSocketAddress bindToAddress = new InetSocketAddress("0.0.0", port);
        server.addEndpoint(new CoapEndpoint(bindToAddress));
    }
}

package org.quister.coap;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CamelCoapResource extends CoapResource {
    private final Logger logger = LoggerFactory.getLogger(CamelCoapResource.class);

    private final Endpoint endpoint;
    private final Processor processor;

    public CamelCoapResource(String name, Endpoint endpoint, Processor processor) {
        super(name);
        this.endpoint = endpoint;
        this.processor = processor;
    }

    @Override
    public void handleRequest(org.eclipse.californium.core.network.Exchange exchange) {
        logger.info("Received Coap request. Converting to camel exchange.");
        CoapExchange coapExchange = new CoapExchange(exchange, this);
        Exchange camelExchange = convertExchange(coapExchange);

        try {
            processor.process(camelExchange);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String body = camelExchange.getIn().getBody(String.class);
        coapExchange.respond(body);
    }

    private Exchange convertExchange(CoapExchange coapExchange) {
        Exchange camelExchange = endpoint.createExchange();

        String body = new String(coapExchange.getRequestPayload());
        camelExchange.getIn().setBody(body);
        camelExchange.getIn().setHeader(Exchange.HTTP_METHOD, "GET");
        return camelExchange;
    }
}

package org.quister.coap;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.http.HttpStatus;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.util.Optional;


class CoapProducer extends DefaultProducer {
    private final Logger logger = LoggerFactory.getLogger(CoapProducer.class);

    private final CamelCoapEndpoint endpoint;
    private final Optional<Long> timeout;

    public CoapProducer(CamelCoapEndpoint endpoint, long timeout) {
        super(endpoint);
        this.endpoint = endpoint;
        this.timeout = Optional.of(timeout);
    }

    public CoapProducer(CamelCoapEndpoint camelCoapEndpoint) {
        super(camelCoapEndpoint);
        this.endpoint = camelCoapEndpoint;
        this.timeout = Optional.empty();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Producing CoAP request");
        CoapClient client = new CoapClient(endpoint.getEndpointUri());
        if (timeout.isPresent()) {
            client.setTimeout(timeout.get());
        }

        CoapResponse response = client.post(exchange.getIn().getBody(byte[].class), MediaTypeRegistry.APPLICATION_OCTET_STREAM);

        if (response == null) {
            client.shutdown();
            logger.warn("No CoAP response received.");
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.SC_GATEWAY_TIMEOUT);
            exchange.getIn().setBody("");
            throw new ConnectException("No CoAP response received");
        } else {
            logger.info("CoAP response received. Status code: " + response.getCode());

            exchange.getIn().setBody(response.getPayload());
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, getHttpStatusCode(response.getCode()));
        }
    }

    private int getHttpStatusCode(CoAP.ResponseCode coapResponseCode) {
        switch(coapResponseCode) {
            case CONTENT:
                return HttpStatus.SC_OK;
            case CREATED:
                return HttpStatus.SC_CREATED;
            case DELETED:
                return HttpStatus.SC_NO_CONTENT;
            case NOT_FOUND:
                return HttpStatus.SC_NOT_FOUND;
            case UNAUTHORIZED:
                return HttpStatus.SC_UNAUTHORIZED;
            case INTERNAL_SERVER_ERROR:
                return HttpStatus.SC_INTERNAL_SERVER_ERROR;
            case GATEWAY_TIMEOUT:
                return HttpStatus.SC_GATEWAY_TIMEOUT;
            default:
                logger.info("Unknown CoAP response code: " + coapResponseCode + ". Returning 200 OK");
                return HttpStatus.SC_OK;
        }
    }
}

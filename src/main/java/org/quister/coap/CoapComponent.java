package org.quister.coap;

import org.apache.camel.*;
import org.apache.camel.impl.DefaultComponent;

import java.util.Map;

public class CoapComponent extends DefaultComponent {

    private final int listenPort;

    public CoapComponent(int listenPort) {
        this.listenPort = listenPort;
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new CamelCoapEndpoint(uri, this, listenPort);
    }
}

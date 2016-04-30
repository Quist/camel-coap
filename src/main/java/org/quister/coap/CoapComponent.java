package org.quister.coap;

import org.apache.camel.*;
import org.apache.camel.impl.DefaultComponent;

import java.util.Map;
import java.util.Optional;

public class CoapComponent extends DefaultComponent {

    private final int listenPort;
    private final Optional<Long> timeout;

    public CoapComponent(int listenPort) {
        this.listenPort = listenPort;
        this.timeout = Optional.empty();
    }

    public CoapComponent(int listenPort, long timeout) {
        this.listenPort = listenPort;
        this.timeout = Optional.of(timeout);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        if (timeout.isPresent()) {
            return new CamelCoapEndpoint(uri, this, listenPort, timeout.get());
        } else {
            return new CamelCoapEndpoint(uri, this, listenPort);
        }
    }
}

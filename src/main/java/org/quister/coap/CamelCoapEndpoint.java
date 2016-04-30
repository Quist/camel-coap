package org.quister.coap;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

import java.util.Optional;

class CamelCoapEndpoint extends DefaultEndpoint {

    private final int port;
    private final Optional<Long> timeout;

    public CamelCoapEndpoint(String uri, CoapComponent coapComponent, int port, long timeout) {
        super(uri, coapComponent);
        this.port = port;
        this.timeout = Optional.of(timeout);
    }

    public CamelCoapEndpoint(String uri, CoapComponent coapComponent, int listenPort) {
        super(uri, coapComponent);
        this.port = listenPort;
        this.timeout = Optional.empty();
    }

    @Override
    public Producer createProducer() throws Exception {
        if (timeout.isPresent()) {
            return new CoapProducer(this, timeout.get());
        } else {
            return new CoapProducer(this);
        }
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new CoapConsumer(this, port, processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

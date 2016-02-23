package org.quister.coap;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

class CamelCoapEndpoint extends DefaultEndpoint {

    private final int port;

    public CamelCoapEndpoint(String uri, CoapComponent coapComponent, int port) {
        super(uri, coapComponent);
        this.port = port;
    }

    @Override
    public Producer createProducer() throws Exception {
        return new CoapProducer(this);
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

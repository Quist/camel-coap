# Apache camel Coap component
A custom [Coap](http://coap.technology/) component. Currently supports receiving HTTP requests and forwarding them to a Coap server.

### Building
`mvn clean install `

## Usage
Include it in your pom.xml:

```xml 

    <dependency>    
        <groupId>your.group.id</groupId>        
        <artifactId>camel-coap</artifactId>        
        <version>0.0-SNAPSHOT</version>        
    </dependency>
```

In your camel application, add it to the camel context:

```java

CoapComponent coap = new CoapComponent(port);
camelContext.addComponent("coap", coap);
```
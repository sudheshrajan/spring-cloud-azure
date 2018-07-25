package com.microsoft.azure.servicebus.stream.binder.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceBusProducerProperties {
    private boolean sync;
    private long sendTimeout = 10000;
}

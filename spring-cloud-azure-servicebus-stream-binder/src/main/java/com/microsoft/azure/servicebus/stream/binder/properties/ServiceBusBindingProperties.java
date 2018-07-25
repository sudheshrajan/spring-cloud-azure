package com.microsoft.azure.servicebus.stream.binder.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceBusBindingProperties {
    private ServiceBusConsumerProperties consumer = new ServiceBusConsumerProperties();
    private ServiceBusProducerProperties producer = new ServiceBusProducerProperties();
}

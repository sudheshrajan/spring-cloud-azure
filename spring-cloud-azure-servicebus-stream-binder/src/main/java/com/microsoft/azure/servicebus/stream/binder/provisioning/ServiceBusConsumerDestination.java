package com.microsoft.azure.servicebus.stream.binder.provisioning;

import org.springframework.cloud.stream.provisioning.ConsumerDestination;

public class ServiceBusConsumerDestination implements ConsumerDestination {

    private String name;

    public ServiceBusConsumerDestination(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}

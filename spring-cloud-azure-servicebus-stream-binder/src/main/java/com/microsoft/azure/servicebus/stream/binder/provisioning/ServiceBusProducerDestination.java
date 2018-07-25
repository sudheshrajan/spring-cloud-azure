package com.microsoft.azure.servicebus.stream.binder.provisioning;

import org.springframework.cloud.stream.provisioning.ProducerDestination;

public class ServiceBusProducerDestination implements ProducerDestination {
    private String name;

    public ServiceBusProducerDestination(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getNameForPartition(int partition) {
        return this.name + "-" + partition;
    }
}

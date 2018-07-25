package com.microsoft.azure.servicebus.stream.binder.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.ExtendedBindingProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@ConfigurationProperties("spring.cloud.stream.servicebus")
public class ServiceBusExtendedBindingProperties
        implements ExtendedBindingProperties<ServiceBusConsumerProperties, ServiceBusProducerProperties> {


    private Map<String, ServiceBusBindingProperties> bindings = new ConcurrentHashMap<>();

    @Override
    public ServiceBusConsumerProperties getExtendedConsumerProperties(String channelName) {
        return this.bindings.computeIfAbsent(channelName, key -> new ServiceBusBindingProperties()).getConsumer();
    }

    @Override
    public ServiceBusProducerProperties getExtendedProducerProperties(String channelName) {
        return this.bindings.computeIfAbsent(channelName, key -> new ServiceBusBindingProperties()).getProducer();
    }

}

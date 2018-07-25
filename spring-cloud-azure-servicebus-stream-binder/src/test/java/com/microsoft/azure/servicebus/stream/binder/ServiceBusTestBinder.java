package com.microsoft.azure.servicebus.stream.binder;

import com.microsoft.azure.servicebus.stream.binder.properties.ServiceBusConsumerProperties;
import com.microsoft.azure.servicebus.stream.binder.properties.ServiceBusProducerProperties;
import com.microsoft.azure.servicebus.stream.binder.provisioning.ServiceBusChannelProvisioner;
import org.springframework.cloud.stream.binder.AbstractTestBinder;
import org.springframework.cloud.stream.binder.BinderHeaders;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.context.support.GenericApplicationContext;

public class ServiceBusTestBinder extends
        AbstractTestBinder<ServiceBusMessageChannelBinder,
                ExtendedConsumerProperties<ServiceBusConsumerProperties>,
                ExtendedProducerProperties<ServiceBusProducerProperties>> {

    public ServiceBusTestBinder() {
        ServiceBusMessageChannelBinder binder = new ServiceBusMessageChannelBinder(BinderHeaders.STANDARD_HEADERS,
                new ServiceBusChannelProvisioner(null, "namespace"), null);
        GenericApplicationContext context = new GenericApplicationContext();
        binder.setApplicationContext(context);
        this.setBinder(binder);
    }

    @Override
    public void cleanup() {

    }
}

package com.microsoft.azure.servicebus.stream.binder.provisioning;

import com.microsoft.azure.servicebus.stream.binder.properties.ServiceBusConsumerProperties;
import com.microsoft.azure.servicebus.stream.binder.properties.ServiceBusProducerProperties;
import com.microsoft.azure.spring.cloud.context.core.AzureAdmin;
import com.microsoft.azure.spring.cloud.context.core.Tuple;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;
import org.springframework.util.Assert;

public class ServiceBusChannelProvisioner implements ProvisioningProvider<ExtendedConsumerProperties<ServiceBusConsumerProperties>,
        ExtendedProducerProperties<ServiceBusProducerProperties>> {

    private final AzureAdmin azureAdmin;
    private final String namespace;

    public ServiceBusChannelProvisioner(AzureAdmin azureAdmin, String namespace) {
        Assert.hasText(namespace, "The namespace can't be null or empty");
        this.azureAdmin = azureAdmin;
        this.namespace = namespace;
    }

    @Override
    public ProducerDestination provisionProducerDestination(String name,
                                                            ExtendedProducerProperties<ServiceBusProducerProperties> properties) throws ProvisioningException {
        this.azureAdmin.getOrCreateEventHub(namespace, name);

        return new ServiceBusProducerDestination(name);
    }

    @Override
    public ConsumerDestination provisionConsumerDestination(String name, String group,
                                                            ExtendedConsumerProperties<ServiceBusConsumerProperties> properties) throws ProvisioningException {
        if (this.azureAdmin.getEventHub(Tuple.of(namespace, name)) == null) {
            throw new ProvisioningException(
                    String.format("Event hub with name '%s' in namespace '%s' not existed", name, namespace));
        }

        this.azureAdmin.getOrCreateEventHubConsumerGroup(namespace, name, group);
        return new ServiceBusConsumerDestination(name);
    }
}

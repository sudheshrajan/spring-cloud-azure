package com.microsoft.azure.servicebus.stream.binder.provisioning;

import com.microsoft.azure.management.servicebus.ServiceBusNamespace;
import com.microsoft.azure.management.servicebus.Topic;
import com.microsoft.azure.servicebus.stream.binder.properties.ServiceBusConsumerProperties;
import com.microsoft.azure.servicebus.stream.binder.properties.ServiceBusProducerProperties;
import com.microsoft.azure.spring.cloud.context.core.AzureAdmin;
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
    public ProducerDestination provisionProducerDestination(String topicName,
                                                            ExtendedProducerProperties<ServiceBusProducerProperties> properties) throws ProvisioningException {
        ServiceBusNamespace serviceBusNamespace = this.azureAdmin.getOrCreateServiceBusNamespace(namespace);
        this.azureAdmin.getOrCreateServiceBusTopic(serviceBusNamespace, topicName);

        return new ServiceBusProducerDestination(topicName);
    }

    @Override
    public ConsumerDestination provisionConsumerDestination(String topicName, String subcriptionGroup,
                                                            ExtendedConsumerProperties<ServiceBusConsumerProperties> properties) throws ProvisioningException {
        Topic topic = this.azureAdmin.getServiceBusTopic(namespace, topicName);
        if (topic == null) {
            throw new ProvisioningException(
                    String.format("Service bus topic with name '%s' in namespace '%s' not existed", topicName, namespace));
        }

        this.azureAdmin.getOrCreateServiceBusTopicSubscription(topic, subcriptionGroup);
        return new ServiceBusConsumerDestination(subcriptionGroup);
    }
}

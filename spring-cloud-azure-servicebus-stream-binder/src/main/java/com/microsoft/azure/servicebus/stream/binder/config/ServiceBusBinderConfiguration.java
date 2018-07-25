package com.microsoft.azure.servicebus.stream.binder.config;

import com.microsoft.azure.servicebus.stream.binder.ServiceBusMessageChannelBinder;
import com.microsoft.azure.servicebus.stream.binder.properties.ServiceBusExtendedBindingProperties;
import com.microsoft.azure.servicebus.stream.binder.provisioning.ServiceBusChannelProvisioner;
import com.microsoft.azure.spring.cloud.autoconfigure.context.AzureContextAutoConfiguration;
import com.microsoft.azure.spring.cloud.autoconfigure.servicebus.AzureServiceBusProperties;
import com.microsoft.azure.spring.cloud.autoconfigure.telemetry.TelemetryAutoConfiguration;
import com.microsoft.azure.spring.cloud.autoconfigure.telemetry.TelemetryTracker;
import com.microsoft.azure.spring.cloud.context.core.AzureAdmin;
import com.microsoft.azure.spring.integration.servicebus.factory.DefaultServiceBusTopicClientFactory;
import com.microsoft.azure.spring.integration.servicebus.factory.ServiceBusTopicClientFactory;
import com.microsoft.azure.spring.integration.servicebus.topic.ServiceBusTopicOperation;
import com.microsoft.azure.spring.integration.servicebus.topic.ServiceBusTopicTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConditionalOnMissingBean(Binder.class)
@AutoConfigureAfter({AzureContextAutoConfiguration.class, TelemetryAutoConfiguration.class})
@EnableConfigurationProperties({AzureServiceBusProperties.class, ServiceBusExtendedBindingProperties.class})
public class ServiceBusBinderConfiguration {

    private static final String SERVICE_BUS_BINDER = "ServiceBusBinder";

    @Autowired(required = false)
    private TelemetryTracker telemetryTracker;

    @PostConstruct
    public void triggerTelemetry() {
        TelemetryTracker.triggerEvent(telemetryTracker, SERVICE_BUS_BINDER);
    }

    @Bean
    public ServiceBusTopicClientFactory clientFactory(AzureAdmin azureAdmin, AzureServiceBusProperties serviceBusProperties,
                                                      ServiceBusExtendedBindingProperties bindingProperties) {
        DefaultServiceBusTopicClientFactory clientFactory =
                new DefaultServiceBusTopicClientFactory(azureAdmin, serviceBusProperties.getNamespace());
        return clientFactory;
    }

    @Bean
    public ServiceBusChannelProvisioner serviceBusChannelProvisioner(AzureAdmin azureAdmin, AzureServiceBusProperties serviceBusProperties) {
        return new ServiceBusChannelProvisioner(azureAdmin, serviceBusProperties.getNamespace());
    }

    @Bean
    public ServiceBusTopicOperation serviceBusTopicOperation(ServiceBusTopicClientFactory clientFactory) {
        return new ServiceBusTopicTemplate(clientFactory);
    }

    @Bean
    public ServiceBusMessageChannelBinder serviceBusBinder(ServiceBusChannelProvisioner serviceBusChannelProvisioner,
                                                           ServiceBusTopicOperation serviceBusTopicOperation) {
        return new ServiceBusMessageChannelBinder(null, serviceBusChannelProvisioner, serviceBusTopicOperation);
    }
}

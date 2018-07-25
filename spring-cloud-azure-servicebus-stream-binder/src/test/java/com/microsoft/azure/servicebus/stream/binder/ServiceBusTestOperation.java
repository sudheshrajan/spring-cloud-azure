package com.microsoft.azure.servicebus.stream.binder;

import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.spring.integration.core.Checkpointer;
import com.microsoft.azure.spring.integration.core.PartitionSupplier;
import com.microsoft.azure.spring.integration.servicebus.topic.ServiceBusTopicOperation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ServiceBusTestOperation implements ServiceBusTopicOperation {
    private final Map<String, Map<String, Consumer<Iterable<IMessage>>>> consumerMap = new ConcurrentHashMap<>();
    private final Map<String, List<IMessage>> serviceBusesByName = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<Void> sendAsync(String serviceBusName, IMessage message,
                                             PartitionSupplier partitionSupplier) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        serviceBusesByName.putIfAbsent(serviceBusName, new LinkedList<>());
        serviceBusesByName.get(serviceBusName).add(message);
        consumerMap.putIfAbsent(serviceBusName, new ConcurrentHashMap<>());
        consumerMap.get(serviceBusName).values().forEach(c -> c.accept(Collections.singleton(message)));
        future.complete(null);
        return future;
    }

    @Override
    public boolean subscribe(String serviceBusName, Consumer<Iterable<IMessage>> consumer,
                             String consumerGroup) {
        consumerMap.putIfAbsent(serviceBusName, new ConcurrentHashMap<>());
        consumerMap.get(serviceBusName).put(consumerGroup, consumer);
        serviceBusesByName.putIfAbsent(serviceBusName, new LinkedList<>());

        return true;
    }

    @Override
    public boolean unsubscribe(String serviceBusName, Consumer<Iterable<IMessage>> consumer,
                               String consumerGroup) {
        consumerMap.get(serviceBusName).remove(consumerGroup);
        return true;
    }

    @Override
    public Checkpointer<IMessage> getCheckpointer(String serviceBusName,
                                                  String consumerGroup) {
        return new ServiceBusTestCheckpointer();
    }
}

package com.microsoft.azure.servicebus.stream.binder;

import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.spring.integration.core.Checkpointer;

import java.util.concurrent.CompletableFuture;

public class ServiceBusTestCheckpointer implements Checkpointer<IMessage> {

    @Override
    public CompletableFuture<Void> checkpoint() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.complete(null);
        return future;
    }

    @Override
    public CompletableFuture<Void> checkpoint(IMessage iMessage) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.complete(null);
        return future;
    }
}

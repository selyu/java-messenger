package org.selyu.messenger.api.model;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class Subscriber {
    private final String publisherName;
    private final Consumer<Object> consumer;

    public Subscriber(@NotNull String publisherName, @NotNull Consumer<Object> consumer) {
        this.publisherName = publisherName;
        this.consumer = consumer;
    }

    public @NotNull String getPublisherName() {
        return publisherName;
    }

    public @NotNull Consumer<Object> getConsumer() {
        return consumer;
    }
}

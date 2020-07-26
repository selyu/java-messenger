package org.selyu.messenger.api.model;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public final class Subscriber {
    private final String publisherName;
    private final Consumer<Object> consumer;

    public Subscriber(String publisherName, Consumer<Object> consumer) {
        requireNonNull(publisherName);
        requireNonNull(consumer);
        this.publisherName = publisherName;
        this.consumer = consumer;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public Consumer<Object> getConsumer() {
        return consumer;
    }
}

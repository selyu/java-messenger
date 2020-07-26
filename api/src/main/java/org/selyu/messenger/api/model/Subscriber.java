package org.selyu.messenger.api.model;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Helper class
 */
public final class Subscriber<T> {
    private final String publisherName;
    private final Consumer<T> onReceiveConsumer;

    public Subscriber(@NotNull String publisherName, @NotNull Consumer<T> onReceiveConsumer) {
        this.publisherName = publisherName;
        this.onReceiveConsumer = onReceiveConsumer;
    }

    public @NotNull String getPublisherName() {
        return publisherName;
    }

    public @NotNull Consumer<T> getOnReceiveConsumer() {
        return onReceiveConsumer;
    }
}

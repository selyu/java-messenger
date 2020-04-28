package org.selyu.messaging.model;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Helper class
 */
public final class Subscriber<T> {
    private final String queue;
    private final Consumer<T> onReceiveConsumer;

    public Subscriber(@NotNull String queue, @NotNull Consumer<T> onReceiveConsumer) {
        this.queue = queue;
        this.onReceiveConsumer = onReceiveConsumer;
    }

    public @NotNull String getQueue() {
        return queue;
    }

    public @NotNull Consumer<T> getOnReceiveConsumer() {
        return onReceiveConsumer;
    }
}

package org.selyu.messaging;

import org.jetbrains.annotations.NotNull;
import org.selyu.messaging.annotation.Subscribe;

/**
 * Responsible for handling subscriptions and sending objects to {@link IPublisher}s
 */
public interface IMessageHandler {
    /**
     * Gets a publisher with the name provided
     *
     * @param name The name
     * @return The publisher implementation
     */
    IPublisher getPublisher(@NotNull String name);

    default IPublisher getPublisher() {
        return getPublisher("all");
    }

    /**
     * Subscribe methods annotated with {@link Subscribe} in the object.
     *
     * @param object The object being subscribed
     */
    <T> void subscribe(@NotNull T object);

    @SuppressWarnings("unchecked")
    default <T> void subscribeMulti(@NotNull T... array) {
        for (T object : array) {
            subscribe(object);
        }
    }

    /**
     * Shutdown method
     */
    void shutdown();
}

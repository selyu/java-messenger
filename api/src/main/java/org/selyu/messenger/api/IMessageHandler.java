package org.selyu.messenger.api;

import org.jetbrains.annotations.NotNull;
import org.selyu.messenger.api.annotation.Subscribe;

/**
 * Handles subscriptions and publishers
 */
public interface IMessageHandler {
    String DEFAULT_CHANNEL = "ALL";

    /**
     * Gets a publisher with the channel name provided
     *
     * @param channel The channel
     * @return The publisher
     */
    IPublisher getPublisher(@NotNull String channel);

    /**
     * Gets a publisher on the {@link IMessageHandler#DEFAULT_CHANNEL} channel
     *
     * @return The publisher
     */
    default IPublisher getPublisher() {
        return getPublisher(DEFAULT_CHANNEL);
    }

    /**
     * Finds every method with annotated with {@link Subscribe} and-
     * subscribes it to the specified channel {@link Subscribe#value()}
     *
     * @param object The object
     */
    void subscribe(@NotNull Object object);

    default void subscribe(@NotNull Object... objects) {
        for (Object object : objects) {
            subscribe(object);
        }
    }

    /**
     * Shuts down all on-going connections (if present)
     */
    void shutdown();
}

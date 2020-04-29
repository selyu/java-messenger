package org.selyu.messaging;

import org.jetbrains.annotations.NotNull;

/**
 * Responsible for handling subscriptions and publishing objects to {@link IQueue}s
 */
public interface IChannel {
    /**
     * Gets a queue with the name provided
     *
     * @param name The name of the queue
     * @return The queue
     */
    IQueue getQueue(@NotNull String name);

    default IQueue getAllQueue() {
        return getQueue("all");
    }

    /**
     * Subscribe methods annotated with {@link org.selyu.messaging.annotation.SubscribeQueue} in the object.
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

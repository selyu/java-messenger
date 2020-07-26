package org.selyu.messenger.api;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Responsible for sending objects to a channel
 * In most cases this will be over a network
 */
public interface IPublisher {
    /**
     * Parses the {@param object} to a String and sends it-
     * to a channel
     *
     * @param object The object
     */
    <T> CompletableFuture<Void> post(@NotNull T object);
}

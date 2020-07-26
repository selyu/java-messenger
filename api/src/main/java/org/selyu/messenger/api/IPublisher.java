package org.selyu.messenger.api;

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
    CompletableFuture<Void> post(Object object);
}

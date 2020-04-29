package org.selyu.messaging;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Responsible for sending objects
 */
public interface IQueue {
    /**
     * "Post" (send) an object through the queue
     *
     * @param object The object being sent
     */
    <T> CompletableFuture<Void> post(@NotNull T object);
}

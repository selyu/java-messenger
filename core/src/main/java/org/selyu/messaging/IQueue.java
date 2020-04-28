package org.selyu.messaging;

import org.jetbrains.annotations.NotNull;

/**
 * Responsible for sending objects
 */
public interface IQueue {
    /**
     * "Post" (send) an object through the queue
     *
     * @param object The object being sent
     */
    <T> void post(@NotNull T object);
}

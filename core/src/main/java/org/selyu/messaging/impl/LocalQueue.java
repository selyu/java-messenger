package org.selyu.messaging.impl;

import org.jetbrains.annotations.NotNull;
import org.selyu.messaging.AbstractQueue;

import java.util.concurrent.CompletableFuture;

final class LocalQueue extends AbstractQueue {
    private final LocalChannel channel;

    public LocalQueue(@NotNull LocalChannel localChannel, @NotNull String queue) {
        super(localChannel, queue);
        channel = localChannel;
    }

    /**
     * Since this isn't used over a network we just send it to the channel to parse!
     */
    @Override
    public CompletableFuture<Void> postData(@NotNull String data) {
        return CompletableFuture.runAsync(() -> channel.parseData(data), executorService);
    }
}

package org.selyu.messaging.impl;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.selyu.messaging.AbstractQueue;

final class LocalQueue extends AbstractQueue {
    private final LocalChannel channel;

    public LocalQueue(@NotNull LocalChannel localChannel, @NotNull Gson gson, @NotNull String queue) {
        super(gson, queue);
        this.channel = localChannel;
    }

    /**
     * Since this isn't used over a network we just send it to the channel to parse!
     */
    @Override
    public <T> void postData(@NotNull String data) {
        channel.parseData(data);
    }
}

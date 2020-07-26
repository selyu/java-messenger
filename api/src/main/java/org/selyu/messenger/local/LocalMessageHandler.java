package org.selyu.messenger.local;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.messenger.api.AbstractMessageHandler;
import org.selyu.messenger.api.IPublisher;

public final class LocalMessageHandler extends AbstractMessageHandler {
    public LocalMessageHandler(@Nullable Gson gson) {
        super(gson);
    }

    void receiveMessage(@NotNull String message) {
        super.parseMessage(message);
    }

    @Override
    public IPublisher getPublisher(@NotNull String channel) {
        return publishers.computeIfAbsent(channel, channel1 -> new LocalPublisher(this, channel));
    }
}

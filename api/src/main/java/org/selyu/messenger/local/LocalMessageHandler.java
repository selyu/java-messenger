package org.selyu.messenger.local;

import com.google.gson.Gson;
import org.selyu.messenger.api.AbstractMessageHandler;
import org.selyu.messenger.api.IPublisher;

import static java.util.Objects.requireNonNull;

public final class LocalMessageHandler extends AbstractMessageHandler {
    public LocalMessageHandler(Gson gson) {
        super(gson);
    }

    void receiveMessage(String message) {
        super.parseMessage(message);
    }

    @Override
    public IPublisher getPublisher(String channel) {
        requireNonNull(channel);
        return publishers.computeIfAbsent(channel, channel1 -> new LocalPublisher(this, channel));
    }
}

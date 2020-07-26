package org.selyu.messenger.local;

import org.jetbrains.annotations.NotNull;
import org.selyu.messenger.api.AbstractPublisher;

final class LocalPublisher extends AbstractPublisher {
    private final LocalMessageHandler messageHandler;

    public LocalPublisher(@NotNull LocalMessageHandler messageHandler, @NotNull String channel) {
        super(messageHandler, channel);
        this.messageHandler = messageHandler;
    }

    @Override
    public void postMessage(@NotNull String message) {
        messageHandler.receiveMessage(message);
    }
}

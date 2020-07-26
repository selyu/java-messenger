package org.selyu.messenger.local;

import org.selyu.messenger.api.AbstractPublisher;

final class LocalPublisher extends AbstractPublisher {
    private final LocalMessageHandler messageHandler;

    public LocalPublisher(LocalMessageHandler messageHandler, String channel) {
        super(messageHandler, channel);
        this.messageHandler = messageHandler;
    }

    @Override
    public void postMessage(String message) {
        messageHandler.receiveMessage(message);
    }
}

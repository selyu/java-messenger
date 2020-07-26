package org.selyu.messenger.local;

import org.jetbrains.annotations.NotNull;
import org.selyu.messenger.api.AbstractPublisher;

final class LocalPublisher extends AbstractPublisher {
    private final LocalMessageHandler channel;

    public LocalPublisher(@NotNull LocalMessageHandler channel, @NotNull String name) {
        super(channel, name);
        this.channel = channel;
    }

    /**
     * Since this isn't used over a network we just send it to the channel to parse!
     */
    @Override
    public void postMessage(@NotNull String message) {
        channel.receiveMessage(message);
    }
}

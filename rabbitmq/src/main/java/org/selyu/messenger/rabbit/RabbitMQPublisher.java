package org.selyu.messenger.rabbit;

import com.rabbitmq.client.Channel;
import org.jetbrains.annotations.NotNull;
import org.selyu.messenger.api.AbstractPublisher;

import java.io.IOException;

final class RabbitMQPublisher extends AbstractPublisher {
    private final Channel rabbitChannel;
    private final RabbitMQMessageHandler messageHandler;

    public RabbitMQPublisher(@NotNull RabbitMQMessageHandler messageHandler, @NotNull Channel rabbitChannel, @NotNull String channel) {
        super(messageHandler, channel);
        this.messageHandler = messageHandler;
        this.rabbitChannel = rabbitChannel;
    }

    @Override
    public void postMessage(@NotNull String message) {
        try {
            rabbitChannel.basicPublish("", messageHandler.rabbitChannel, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

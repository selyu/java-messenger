package org.selyu.messenger.rabbit;

import com.rabbitmq.client.Channel;
import org.jetbrains.annotations.NotNull;
import org.selyu.messenger.api.AbstractPublisher;

import java.io.IOException;

final class RabbitMQPublisher extends AbstractPublisher {
    private final Channel channel;
    private final RabbitMQMessageHandler messageHandler;

    public RabbitMQPublisher(@NotNull RabbitMQMessageHandler messageHandler, @NotNull Channel channel, @NotNull String name) {
        super(messageHandler, name);
        this.messageHandler = messageHandler;
        this.channel = channel;
    }

    @Override
    public void postMessage(@NotNull String message) {
        try {
            channel.basicPublish("", messageHandler.rabbitChannel, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

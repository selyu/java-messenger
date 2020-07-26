package org.selyu.messenger.rabbit;

import com.rabbitmq.client.Channel;
import org.selyu.messenger.api.AbstractPublisher;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

final class RabbitMQPublisher extends AbstractPublisher {
    private final Channel rabbitChannel;
    private final RabbitMQMessageHandler messageHandler;

    public RabbitMQPublisher(RabbitMQMessageHandler messageHandler, Channel rabbitChannel, String channel) {
        super(messageHandler, channel);
        requireNonNull(rabbitChannel);
        this.messageHandler = messageHandler;
        this.rabbitChannel = rabbitChannel;
    }

    @Override
    public void postMessage(String message) {
        requireNonNull(message);
        try {
            rabbitChannel.basicPublish("", messageHandler.rabbitChannel, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

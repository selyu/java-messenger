package org.selyu.messenger.rabbit;

import com.rabbitmq.client.Channel;
import org.jetbrains.annotations.NotNull;
import org.selyu.messenger.api.AbstractPublisher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.CompletableFuture;

final class RabbitMQPublisher extends AbstractPublisher {
    private final Channel channel;
    private final RabbitMQMessageHandler messageHandler;

    public RabbitMQPublisher(@NotNull RabbitMQMessageHandler messageHandler, @NotNull Channel channel, @NotNull String name) {
        super(messageHandler, name);
        this.messageHandler = messageHandler;
        this.channel = channel;
    }

    @Override
    public CompletableFuture<Void> postData(@NotNull String data) {
        return CompletableFuture.runAsync(() -> {
            String encodedData = null;
            try {
                encodedData = URLEncoder.encode(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                channel.basicPublish("", messageHandler.rabbitChannel, null, encodedData.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, executorService);
    }
}

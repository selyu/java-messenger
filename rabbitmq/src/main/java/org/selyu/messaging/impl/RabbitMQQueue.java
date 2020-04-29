package org.selyu.messaging.impl;

import com.rabbitmq.client.Channel;
import org.jetbrains.annotations.NotNull;
import org.selyu.messaging.AbstractQueue;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

final class RabbitMQQueue extends AbstractQueue {
    private final Channel channel;
    private final RabbitMQChannel rabbitMQChannel;

    public RabbitMQQueue(RabbitMQChannel rabbitMQChannel, Channel channel, String queue) {
        super(rabbitMQChannel, queue);
        this.rabbitMQChannel = rabbitMQChannel;
        this.channel = channel;
    }

    @Override
    public CompletableFuture<Void> postData(@NotNull String data) {
        return CompletableFuture.runAsync(() -> {
            String encodedData = URLEncoder.encode(data, StandardCharsets.UTF_8);
            try {
                channel.basicPublish("", rabbitMQChannel.rabbitChannel, null, encodedData.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, executorService);
    }
}

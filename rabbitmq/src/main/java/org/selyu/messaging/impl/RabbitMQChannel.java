package org.selyu.messaging.impl;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.messaging.AbstractChannel;
import org.selyu.messaging.IQueue;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public final class RabbitMQChannel extends AbstractChannel {
    protected final String rabbitChannel;
    private final Channel channel;
    private final Connection connection;

    public RabbitMQChannel(@NotNull ConnectionFactory factory, @NotNull String rabbitChannel, @Nullable Gson gson) throws IOException, TimeoutException {
        super(gson == null ? new Gson() : gson);
        this.rabbitChannel = rabbitChannel;

        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(rabbitChannel, false, false, false, null);
        channel.basicConsume(rabbitChannel, true, (s, delivery) -> {
            String data = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String decodedData = URLDecoder.decode(data, StandardCharsets.UTF_8);
            parseData(decodedData);
        }, ignored -> {
        });
    }

    @Override
    public IQueue getQueue(@NotNull String name) {
        return queues.computeIfAbsent(name, queue -> new RabbitMQQueue(this, channel, queue));
    }

    @Override
    public void shutdown() {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.shutdown();
    }
}

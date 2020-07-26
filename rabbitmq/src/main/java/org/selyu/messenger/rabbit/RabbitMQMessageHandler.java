package org.selyu.messenger.rabbit;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.messenger.api.AbstractMessageHandler;
import org.selyu.messenger.api.IPublisher;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public final class RabbitMQMessageHandler extends AbstractMessageHandler {
    protected final String rabbitChannel;
    private final Channel channel;
    private final Connection connection;

    public RabbitMQMessageHandler(@NotNull ConnectionFactory factory, @NotNull String rabbitChannel, @Nullable Gson gson) throws IOException, TimeoutException {
        super(gson);
        this.rabbitChannel = rabbitChannel;

        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(rabbitChannel, false, false, false, null);
        channel.basicConsume(rabbitChannel, true, (s, delivery) -> {
            String data = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String decodedData = URLDecoder.decode(data, "UTF-8");
            parseData(decodedData);
        }, ignored -> {
        });
    }

    @Override
    public IPublisher getPublisher(@NotNull String name) {
        return publishers.computeIfAbsent(name, name1 -> new RabbitMQPublisher(this, channel, name));
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

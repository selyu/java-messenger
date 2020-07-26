package org.selyu.messenger.rabbit;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.selyu.messenger.api.AbstractMessageHandler;
import org.selyu.messenger.api.IPublisher;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static java.util.Objects.requireNonNull;

public final class RabbitMQMessageHandler extends AbstractMessageHandler {
    protected final String rabbitChannel;
    private final Channel channel;
    private final Connection connection;

    public RabbitMQMessageHandler(ConnectionFactory factory, String rabbitChannel, Gson gson) throws IOException, TimeoutException {
        super(gson);
        requireNonNull(factory, rabbitChannel);
        this.rabbitChannel = rabbitChannel;

        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(rabbitChannel, false, false, false, null);
        channel.basicConsume(rabbitChannel, true, (s, delivery) -> {
            String data = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String decodedData = URLDecoder.decode(data, "UTF-8");
            parseMessage(decodedData);
        }, ignored -> {
        });
    }

    @Override
    public IPublisher getPublisher(String channel) {
        requireNonNull(channel);
        return publishers.computeIfAbsent(channel, channel1 -> new RabbitMQPublisher(this, this.channel, channel));
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

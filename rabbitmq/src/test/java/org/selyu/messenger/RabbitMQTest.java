package org.selyu.messenger;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;
import org.selyu.messaging.Message;
import org.selyu.messaging.TestSubscriber;
import org.selyu.messaging.impl.RabbitMQChannel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class RabbitMQTest {
    @Test
    public void run() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new MockConnectionFactory();
        RabbitMQChannel channel = new RabbitMQChannel(factory, "test", null);
        channel.subscribe(TestSubscriber.getInstance());
        channel.getAllQueue().post(new Message("RabbitMQ!!"));
        Thread.sleep(100);
    }
}

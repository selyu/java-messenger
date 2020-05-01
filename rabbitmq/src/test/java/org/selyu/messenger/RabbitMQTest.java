package org.selyu.messenger;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;
import org.selyu.messaging.Message;
import org.selyu.messaging.TestSubscriber;
import org.selyu.messaging.impl.RabbitMQMessageHandler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class RabbitMQTest {
    @Test
    public void run() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new MockConnectionFactory();
        RabbitMQMessageHandler channel = new RabbitMQMessageHandler(factory, "test", null);
        channel.subscribe(TestSubscriber.getInstance());
        channel.getPublisher().post(new Message("RabbitMQ!!"));
        Thread.sleep(100);
    }
}

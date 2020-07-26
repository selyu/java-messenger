package org.selyu.messenger;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;
import org.selyu.messaging.annotation.Subscribe;
import org.selyu.messaging.impl.RabbitMQMessageHandler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class RabbitMQTest {
    @Test
    public void run() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new MockConnectionFactory();
        RabbitMQMessageHandler channel = new RabbitMQMessageHandler(factory, "test", null);
        channel.subscribe(new Object() {
            @Subscribe
            public void hello(String string) {
                System.out.println(string);
            }
        });
        channel.getPublisher().post("Hello World! from RabbitMQ");

        Thread.sleep(100);
    }
}

package org.selyu.messenger;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.selyu.messenger.api.annotation.Subscribe;
import org.selyu.messenger.rabbit.RabbitMQMessageHandler;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;

public final class RabbitMQTest {
    private ConnectionFactory connectionFactory;
    private RabbitMQMessageHandler messageHandler;
    private CountDownLatch countDownLatch;

    @Before
    public void before() throws IOException, TimeoutException {
        connectionFactory = new MockConnectionFactory();
        messageHandler = new RabbitMQMessageHandler(connectionFactory, "test", null);
        countDownLatch = new CountDownLatch(1);
    }

    @Test
    public void run() throws InterruptedException {
        boolean[] received = {false};
        messageHandler.subscribe(new Object() {
            @Subscribe
            public void hello(String string) {
                received[0] = true;
                countDownLatch.countDown();
            }

            @Subscribe("NOT_ALL")
            public void goodbye(String string) throws Exception {
                throw new Exception("I shouldn't receive this!");
            }
        });
        messageHandler.getPublisher().post("Hello World!");
        countDownLatch.await(1, TimeUnit.SECONDS);
        assertTrue(received[0]);
    }

    @After
    public void after() {
        messageHandler.shutdown();
    }
}

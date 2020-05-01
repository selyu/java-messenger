package org.selyu.messaging;

import com.github.fppt.jedismock.RedisServer;
import com.github.fppt.jedismock.server.ServiceOptions;
import org.junit.Test;
import org.selyu.messaging.impl.JedisMessageHandler;

import java.io.IOException;
import java.net.URISyntaxException;

public final class JedisTest {
    @Test
    public void run() throws IOException, URISyntaxException, InterruptedException {
        RedisServer server = RedisServer.newRedisServer(9999);
        ServiceOptions options = new ServiceOptions() {
            @Override
            public int autoCloseOn() {
                return 3;
            }
        };
        server.setOptions(options);
        server.start();

        IMessageHandler channel = new JedisMessageHandler("redis://127.0.0.1:9999/0", "test", null);
        channel.subscribe(TestSubscriber.getInstance());
        channel.getPublisher().post(new Message("Hello World! from Jedis"));
        Thread.sleep(100);

        channel.shutdown();
        server.stop();
    }
}

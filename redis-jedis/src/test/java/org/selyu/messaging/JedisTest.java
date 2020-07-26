package org.selyu.messaging;

import com.github.fppt.jedismock.RedisServer;
import com.github.fppt.jedismock.server.ServiceOptions;
import org.junit.Test;
import org.selyu.messaging.annotation.Subscribe;
import org.selyu.messaging.impl.JedisMessageHandler;

import java.io.IOException;
import java.net.URISyntaxException;

public final class JedisTest {
    private static int MOCK_PORT = 9999;

    @Test
    public void run() throws IOException, URISyntaxException, InterruptedException {
        RedisServer server = RedisServer.newRedisServer(MOCK_PORT);
        ServiceOptions options = new ServiceOptions() {
            @Override
            public int autoCloseOn() {
                return 3;
            }
        };
        server.setOptions(options);
        server.start();

        IMessageHandler channel = new JedisMessageHandler("redis://127.0.0.1:9999/0", "test", null);
        channel.subscribe(new Object() {
            @Subscribe
            public void helloWorld(String string) {
                System.out.println(string);
            }

            @Subscribe("NOT_ALL")
            public void goodbyeWorld(String string) throws Exception {
                System.out.println("GOODBYE: " + string);
                throw new Exception("I should not receive this!");
            }
        });
        channel.getPublisher().post("Hello World! from Jedis");
        Thread.sleep(250);

        channel.shutdown();
        server.stop();
    }
}

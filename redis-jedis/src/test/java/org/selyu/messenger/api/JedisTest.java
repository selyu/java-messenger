package org.selyu.messenger.api;

import com.github.fppt.jedismock.RedisServer;
import com.github.fppt.jedismock.server.ServiceOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.selyu.messenger.api.annotation.Subscribe;
import org.selyu.messenger.redis.JedisMessageHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public final class JedisTest {
    private static int MOCK_PORT = 9999;

    private RedisServer server;
    private JedisMessageHandler jedisMessageHandler;
    private CountDownLatch countDownLatch;

    @Before
    public void before() throws IOException, URISyntaxException {
        server = RedisServer.newRedisServer(MOCK_PORT);
        server.setOptions(new ServiceOptions() {
            @Override
            public int autoCloseOn() {
                return 3;
            }
        });
        server.start();

        jedisMessageHandler = new JedisMessageHandler(String.format("redis://127.0.0.1:%s/0", MOCK_PORT), "test", null);
        countDownLatch = new CountDownLatch(1);
    }

    @Test
    public void run() throws InterruptedException {
        boolean[] received = new boolean[]{false};
        jedisMessageHandler.subscribe(new Object() {
            @Subscribe
            public void helloWorld(String string) {
                received[0] = true;
                countDownLatch.countDown();
            }

            @Subscribe("NOT_ALL")
            public void goodbyeWorld(String string) throws Exception {
                throw new Exception("I should not receive this!");
            }
        });
        jedisMessageHandler.getPublisher().post("Hello World! from Jedis");
        countDownLatch.await(1, TimeUnit.SECONDS);
        assertTrue(received[0]);
    }

    @After
    public void after() {
        jedisMessageHandler.shutdown();
        server.stop();
    }
}

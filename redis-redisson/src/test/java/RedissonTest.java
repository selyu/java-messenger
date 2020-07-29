import com.github.fppt.jedismock.RedisServer;
import com.github.fppt.jedismock.server.ServiceOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.RedissonNodeConfig;
import org.selyu.messenger.api.annotation.Subscribe;
import org.selyu.messenger.redis.RedissonMessageHandler;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class RedissonTest {
    private RedisServer redisServer;
    private RedissonMessageHandler redissonMessageHandler;
    private CountDownLatch countDownLatch;

    @Before
    public void before() throws IOException {
        redisServer = RedisServer.newRedisServer(9999);
        redisServer.setOptions(new ServiceOptions() {
            @Override
            public int autoCloseOn() {
                return 3;
            }
        });
        redisServer.start();

        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:" + 9999);

        redissonMessageHandler = new RedissonMessageHandler(Redisson.create(config), "test", null);
        countDownLatch = new CountDownLatch(1);
    }

    @Test
    public void run() throws InterruptedException {
        boolean[] received = new boolean[]{false};
        redissonMessageHandler.subscribe(new Object() {
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
        redissonMessageHandler.getPublisher().post("Hello World! from Redisson");
        countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(received[0]);
    }

    @After
    public void after() {
        redisServer.stop();
    }
}

package org.selyu.messenger.redis;

import com.google.gson.Gson;
import org.selyu.messenger.api.AbstractMessageHandler;
import org.selyu.messenger.api.IPublisher;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.util.Pool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

public final class JedisMessageHandler extends AbstractMessageHandler {
    private final Pool<Jedis> pool;
    private final String redisChannel;
    private final JedisPubSub pubSub;

    public JedisMessageHandler(String redisURL, String redisChannel, Gson gson) throws URISyntaxException {
        this(new JedisPool(new URI(redisURL)), redisChannel, gson);
    }

    public JedisMessageHandler(JedisPool pool, String redisChannel, Gson gson) {
        super(gson);
        requireNonNull(pool);
        requireNonNull(redisChannel);

        this.pool = pool;
        this.redisChannel = redisChannel;

        AtomicBoolean subscribed = new AtomicBoolean(false);
        try (Jedis jedis = pool.getResource()) {
            jedis.ping();
        }

        pubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                parseMessage(message);
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                subscribed.set(true);
            }
        };
        executorService.submit(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.subscribe(pubSub, redisChannel);
            }
        });

        while (!subscribed.get()) {
        }
    }

    @Override
    public IPublisher getPublisher(String channel) {
        requireNonNull(channel);
        return publishers.computeIfAbsent(channel, channel1 -> new JedisPublisher(this, pool, redisChannel, channel));
    }

    @Override
    public void shutdown() {
        pubSub.unsubscribe();
        pool.close();
        super.shutdown();
    }
}

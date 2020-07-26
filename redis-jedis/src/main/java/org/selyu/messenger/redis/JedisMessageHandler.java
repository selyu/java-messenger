package org.selyu.messenger.redis;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.messenger.api.AbstractMessageHandler;
import org.selyu.messenger.api.IPublisher;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.util.Pool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class JedisMessageHandler extends AbstractMessageHandler {
    private final Pool<Jedis> pool;
    private final String redisChannel;
    private final ChannelPubSub pubSub;

    public JedisMessageHandler(@NotNull String redisURL, @NotNull String redisChannel, @Nullable Gson gson) throws URISyntaxException {
        this(new JedisPool(new URI(redisURL)), redisChannel, gson);
    }

    public JedisMessageHandler(@NotNull JedisPool pool, @NotNull String redisChannel, @Nullable Gson gson) {
        super(gson);
        this.pool = pool;
        this.redisChannel = redisChannel;

        AtomicBoolean subscribed = new AtomicBoolean(false);
        try (Jedis jedis = pool.getResource()) {
            jedis.ping();
        }

        pubSub = new ChannelPubSub(this, () -> subscribed.set(true));
        executorService.submit(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.subscribe(pubSub, redisChannel);
            }
        });

        //noinspection StatementWithEmptyBody
        while (!subscribed.get()) {
        }
    }

    @Override
    public IPublisher getPublisher(@NotNull String channel) {
        return publishers.computeIfAbsent(channel, name1 -> new JedisPublisher(this, pool, redisChannel, channel));
    }

    @Override
    public void shutdown() {
        pubSub.unsubscribe();
        pool.close();
        super.shutdown();
    }
}

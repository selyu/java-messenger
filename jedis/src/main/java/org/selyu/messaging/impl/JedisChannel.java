package org.selyu.messaging.impl;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.messaging.AbstractChannel;
import org.selyu.messaging.IQueue;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.util.Pool;

import java.net.URI;
import java.net.URISyntaxException;

public final class JedisChannel extends AbstractChannel {
    private final Pool<Jedis> pool;
    private final String redisChannel;
    private final ChannelPubSub pubSub;

    @SuppressWarnings({"StatementWithEmptyBody", "LoopConditionNotUpdatedInsideLoop"})
    public JedisChannel(@NotNull String redisURL, @NotNull String redisChannel, @Nullable Gson gson) throws URISyntaxException {
        super(gson == null ? new Gson() : gson);
        this.redisChannel = redisChannel;

        pool = new JedisPool(new URI(redisURL));
        final boolean[] subscribed = {false};

        try (Jedis jedis = pool.getResource()) {
            jedis.ping();
        }

        pubSub = new ChannelPubSub(this, () -> subscribed[0] = true);
        executorService.submit(() -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.subscribe(pubSub, redisChannel);
            }
        });

        // Block thread until subscribed
        while (!subscribed[0]) {
        }
    }

    @Override
    public IQueue getQueue(@NotNull String name) {
        return queues.computeIfAbsent(name, queue -> new JedisQueue(this, pool, redisChannel, queue));
    }

    @Override
    public void shutdown() {
        pubSub.unsubscribe();
        pool.close();
        super.shutdown();
    }
}

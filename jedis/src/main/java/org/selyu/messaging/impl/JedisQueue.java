package org.selyu.messaging.impl;

import org.jetbrains.annotations.NotNull;
import org.selyu.messaging.AbstractQueue;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

import java.util.concurrent.CompletableFuture;

final class JedisQueue extends AbstractQueue {
    private final Pool<Jedis> pool;
    private final String redisChannel;

    public JedisQueue(@NotNull JedisChannel channel, @NotNull Pool<Jedis> pool, @NotNull String redisChannel, @NotNull String queue) {
        super(channel, queue);
        this.pool = pool;
        this.redisChannel = redisChannel;
    }

    @Override
    public CompletableFuture<Void> postData(@NotNull String data) {
        return CompletableFuture.runAsync(() -> {
            try (Jedis jedis = pool.getResource()) {
                long result = jedis.publish(redisChannel, data);
                if (result == 0) jedis.publish(redisChannel, data);
            }
        }, executorService).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}

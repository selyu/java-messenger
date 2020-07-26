package org.selyu.messenger.redis;

import org.jetbrains.annotations.NotNull;
import org.selyu.messenger.api.AbstractPublisher;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

final class JedisPublisher extends AbstractPublisher {
    private final Pool<Jedis> pool;
    private final String redisChannel;

    public JedisPublisher(@NotNull JedisMessageHandler messageHandler, @NotNull Pool<Jedis> pool, @NotNull String redisChannel, @NotNull String channel) {
        super(messageHandler, channel);
        this.pool = pool;
        this.redisChannel = redisChannel;
    }

    @Override
    public void postMessage(@NotNull String message) {
        try (Jedis jedis = pool.getResource()) {
            long result = jedis.publish(redisChannel, message);
            if (result == 0)
                jedis.publish(redisChannel, message);
        }
    }
}

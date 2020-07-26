package org.selyu.messenger.redis;

import org.selyu.messenger.api.AbstractPublisher;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

import static java.util.Objects.requireNonNull;

final class JedisPublisher extends AbstractPublisher {
    private final Pool<Jedis> pool;
    private final String redisChannel;

    public JedisPublisher(JedisMessageHandler messageHandler, Pool<Jedis> pool, String redisChannel, String channel) {
        super(messageHandler, channel);
        requireNonNull(pool);
        requireNonNull(redisChannel);
        this.pool = pool;
        this.redisChannel = redisChannel;
    }

    @Override
    public void postMessage(String message) {
        requireNonNull(message);
        try (Jedis jedis = pool.getResource()) {
            long result = jedis.publish(redisChannel, message);
            if (result == 0)
                jedis.publish(redisChannel, message);
        }
    }
}

package org.selyu.messenger.redis;

import com.google.gson.Gson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.selyu.messenger.api.AbstractMessageHandler;
import org.selyu.messenger.api.AbstractPublisher;
import org.selyu.messenger.api.IPublisher;

import static java.util.Objects.requireNonNull;

public final class RedissonMessageHandler extends AbstractMessageHandler {
    private final RedissonClient redissonClient;
    private final RTopic topic;

    public RedissonMessageHandler(RedissonClient redissonClient, String redisChannel, Gson gson) {
        super(gson);
        requireNonNull(redissonClient);
        requireNonNull(redisChannel);

        this.redissonClient = redissonClient;
        topic = redissonClient.getTopic(redisChannel);
        topic.addListener(String.class, ((channel, message) -> parseMessage(message)));
    }

    @Override
    public IPublisher getPublisher(String channel) {
        return publishers.computeIfAbsent(channel, channel1 -> new AbstractPublisher(this, channel) {
            @Override
            protected void postMessage(String message) {
                topic.publish(message);
            }
        });
    }

    @Override
    public void shutdown() {
        redissonClient.shutdown();
        super.shutdown();
    }
}

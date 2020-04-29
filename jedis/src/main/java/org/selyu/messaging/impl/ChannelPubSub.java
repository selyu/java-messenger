package org.selyu.messaging.impl;

import redis.clients.jedis.JedisPubSub;

final class ChannelPubSub extends JedisPubSub {
    private final JedisChannel jedisChannel;
    private final Runnable onSubscribe;

    ChannelPubSub(JedisChannel jedisChannel, Runnable onSubscribe) {
        this.jedisChannel = jedisChannel;
        this.onSubscribe = onSubscribe;
    }

    @Override
    public void onMessage(String channel, String message) {
        jedisChannel.parseData(message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        onSubscribe.run();
    }
}

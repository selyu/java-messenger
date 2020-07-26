package org.selyu.messaging.impl;

import redis.clients.jedis.JedisPubSub;

final class ChannelPubSub extends JedisPubSub {
    private final JedisMessageHandler messageHandler;
    private final Runnable onSubscribe;

    ChannelPubSub(JedisMessageHandler messageHandler, Runnable onSubscribe) {
        this.messageHandler = messageHandler;
        this.onSubscribe = onSubscribe;
    }

    @Override
    public void onMessage(String channel, String message) {
        messageHandler.parseData(message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        onSubscribe.run();
    }
}

package org.selyu.messaging;

import org.selyu.messaging.annotation.Subscribe;

public final class TestSubscriber {
    private static TestSubscriber INSTANCE;

    public static TestSubscriber getInstance() {
        if(INSTANCE == null)
            INSTANCE = new TestSubscriber();
        return INSTANCE;
    }

    @Subscribe
    public void onMessageAll(Message message) {
        System.out.println("Got message:" + message.getText());
    }
}

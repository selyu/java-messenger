package org.selyu.messaging;

import org.junit.Test;
import org.selyu.messaging.annotation.Subscribe;
import org.selyu.messaging.impl.LocalMessageHandler;

public class LocalTest {
    @Test
    public void run() throws InterruptedException {
        IMessageHandler channel = new LocalMessageHandler(null);
        channel.subscribe(this);
        channel.getPublisher().post("Hello World!");
        Thread.sleep(100);
    }

    @Subscribe
    public void onMessage(String hi) {
        System.out.println(hi);
    }
}

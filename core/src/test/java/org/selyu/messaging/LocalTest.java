package org.selyu.messaging;

import org.junit.Test;
import org.selyu.messaging.annotation.SubscribeQueue;
import org.selyu.messaging.impl.LocalChannel;

public class LocalTest {
    @Test
    public void run() throws InterruptedException {
        IChannel channel = new LocalChannel(null);
        channel.subscribe(this);
        channel.getAllQueue().post("Hello World!");
        Thread.sleep(100);
    }

    @SubscribeQueue
    public void onMessage(String hi) {
        System.out.println(hi);
    }
}

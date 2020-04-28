package org.selyu.messaging;

import org.junit.Test;
import org.selyu.messaging.annotation.SubscribeQueue;
import org.selyu.messaging.impl.LocalChannel;
import org.selyu.messaging.model.Message;

public class LocalTest {
    @Test
    public void run() {
        IChannel channel = new LocalChannel(null);
        channel.subscribe(this);
        channel.getAllQueue().post(new Message("Hello World!"));
        channel.getQueue("test").post(new Message("Hello World2!"));
        channel.getQueue("test").post(new Message("Hello World3!"));
        channel.getQueue("test").post(new Message("Hello World4!"));
        channel.getQueue("test").post(new Message("Hello World5!"));
        channel.getQueue("test").post(new Message("Hello World6!"));
        channel.getQueue("test").post(new Message("Hello World7!"));
        channel.getQueue("test").post(new Message("Hello World8!"));
    }

    @SubscribeQueue("test")
    public void onMessage(Message message) {
        System.out.println("Got message: " + message.getText());
    }
}

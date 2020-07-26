package org.selyu.messenger.api;

import com.google.gson.Gson;
import org.selyu.messenger.api.annotation.Subscribe;
import org.selyu.messenger.api.model.PostedMessage;
import org.selyu.messenger.api.model.Subscriber;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.requireNonNull;

public abstract class AbstractMessageHandler implements IMessageHandler {
    protected final Gson gson;
    protected final Map<Class<?>, Set<Subscriber>> subscribers = new HashMap<>();
    protected final Map<String, IPublisher> publishers = new HashMap<>();
    protected final ExecutorService executorService = Executors.newFixedThreadPool(2);

    protected AbstractMessageHandler(Gson gson) {
        this.gson = gson == null ? new Gson() : gson;
    }

    protected void parseMessage(String message) {
        requireNonNull(message);

        try {
            PostedMessage<?> postedMessage = PostedMessage.deserialize(message, gson);
            if (postedMessage == null)
                return;

            Set<Subscriber> subscribers = this.subscribers.get(postedMessage.getType());
            if (subscribers != null) {
                for (Subscriber subscriber : subscribers) {
                    if (subscriber.getPublisherName().equals(postedMessage.getChannel())) {
                        subscriber.getConsumer().accept(postedMessage.getInstance());
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subscribe(Object object) {
        requireNonNull(object);

        for (Method method : object.getClass().getMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class) || method.getParameterCount() != 1)
                continue;
            method.setAccessible(true);

            Subscribe annotation = method.getAnnotation(Subscribe.class);
            Set<Subscriber> subscriberSet = subscribers.get(method.getParameterTypes()[0]) == null ? new HashSet<>() : subscribers.get(method.getParameterTypes()[0]);

            subscriberSet.add(new Subscriber(annotation.value(), (obj) -> {
                try {
                    method.invoke(object, obj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }));

            subscribers.put(method.getParameterTypes()[0], subscriberSet);
        }
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }
}

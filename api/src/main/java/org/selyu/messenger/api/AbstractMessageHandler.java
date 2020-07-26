package org.selyu.messenger.api;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.messenger.api.annotation.Subscribe;
import org.selyu.messenger.api.model.Subscriber;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractMessageHandler implements IMessageHandler {
    protected final Gson gson;
    protected final Map<Class<?>, Set<Subscriber<?>>> subscribers = new HashMap<>();
    protected final Map<String, IPublisher> publishers = new HashMap<>();
    protected final ExecutorService executorService = Executors.newFixedThreadPool(2);

    protected AbstractMessageHandler(@Nullable Gson gson) {
        this.gson = gson == null ? new Gson() : gson;
    }

    public void parseData(@NotNull String data) {
        // Data is put into a string as: CLASS_NAME, GSON DATA, QUEUE NAME
        String[] split = data.split("@", 3);
        if (split.length < 3) {
            return;
        }

        Class<?> type;

        try {
            type = Class.forName(split[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Get subscribers for type, if null do nothing, else loop through and check if the queue's match, if so get an object from gson and accept the consumer, else nothing
        Set<Subscriber<?>> subscribers = this.subscribers.get(type);
        if (subscribers != null) {
            subscribers.forEach(subscriber -> {
                if (subscriber.getPublisherName().equals(split[2])) {
                    subscriber.getOnReceiveConsumer().accept(gson.fromJson(split[1], (Type) type));
                }
            });
        }
    }

    @Override
    public void subscribe(@NotNull Object object) {
        for (Method method : object.getClass().getMethods()) {
            method.setAccessible(true);

            if (!method.isAnnotationPresent(Subscribe.class)) continue;
            if (method.getParameterCount() != 1) continue;

            Subscribe annotation = method.getAnnotation(Subscribe.class);
            Set<Subscriber<?>> subscriberSet = subscribers.get(method.getParameterTypes()[0]) == null ? new HashSet<>() : subscribers.get(method.getParameterTypes()[0]);

            subscriberSet.add(new Subscriber<>(annotation.value(), (obj) -> {
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

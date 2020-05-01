package org.selyu.messaging;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.messaging.annotation.Subscribe;
import org.selyu.messaging.model.Subscriber;

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
    public <T> void subscribe(@NotNull T object) {
        for (Method m : object.getClass().getMethods()) {
            m.setAccessible(true);

            if (!m.isAnnotationPresent(Subscribe.class)) continue;
            if (m.getParameterCount() != 1) continue;

            Subscribe annotation = m.getAnnotation(Subscribe.class);
            Set<Subscriber<?>> subscriberSet = subscribers.get(m.getParameterTypes()[0]) == null ? new HashSet<>() : subscribers.get(m.getParameterTypes()[0]);

            subscriberSet.add(new Subscriber<>(annotation.value(), (obj) -> {
                try {
                    m.invoke(object, obj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }));

            subscribers.put(m.getParameterTypes()[0], subscriberSet);
        }
    }

    /**
     * When extending this class always call {@code super.shutdown()}
     */
    @Override
    public void shutdown() {
        executorService.shutdown();
    }
}

package org.selyu.messenger.api.model;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static java.util.Objects.requireNonNull;

public final class PostedMessage<T> {
    private final Class<T> type;
    private final T instance;
    private final String channel;

    private PostedMessage(Class<T> type, T instance, String channel) {
        requireNonNull(type);
        requireNonNull(instance);
        requireNonNull(channel);
        this.type = type;
        this.instance = instance;
        this.channel = channel;
    }

    public static String serialize(Object object, String channel, Gson gson) throws UnsupportedEncodingException {
        requireNonNull(object);
        requireNonNull(channel);
        requireNonNull(gson);

        String json = URLEncoder.encode(gson.toJson(object), "UTF-8");
        return String.format("%s@%s@%s", object.getClass().getName(), json, channel);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static PostedMessage<?> deserialize(String data, Gson gson) throws UnsupportedEncodingException {
        requireNonNull(data);
        requireNonNull(gson);

        String[] split = data.split("@", 3);
        if (split.length < 3) {
            return null;
        }

        Class<?> type;
        try {
            type = Class.forName(split[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        String decodedJson = URLDecoder.decode(split[1], "UTF-8");
        String channel = split[2];
        return new PostedMessage(type, gson.fromJson(decodedJson, type), channel);
    }

    public Class<T> getType() {
        return type;
    }

    public T getInstance() {
        return instance;
    }

    public String getChannel() {
        return channel;
    }
}

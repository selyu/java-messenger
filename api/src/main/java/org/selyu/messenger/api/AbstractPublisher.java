package org.selyu.messenger.api;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.selyu.messenger.api.model.PostedMessage;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public abstract class AbstractPublisher implements IPublisher {
    private final ExecutorService executorService;
    private final Gson gson;
    private final String name;

    public AbstractPublisher(@NotNull AbstractMessageHandler channel, @NotNull String name) {
        this.executorService = channel.executorService;
        this.gson = channel.gson;
        this.name = name;
    }

    protected abstract void postMessage(@NotNull String message);

    @Override
    public <T> CompletableFuture<Void> post(@NotNull T object) {
        return CompletableFuture.runAsync(() -> {
            try {
                String message = PostedMessage.serialize(object, name, gson);
                postMessage(message);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }, executorService).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}

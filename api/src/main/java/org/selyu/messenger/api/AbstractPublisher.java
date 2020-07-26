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
    private final String channel;

    public AbstractPublisher(@NotNull AbstractMessageHandler messageHandler, @NotNull String channel) {
        this.executorService = messageHandler.executorService;
        this.gson = messageHandler.gson;
        this.channel = channel;
    }

    protected abstract void postMessage(@NotNull String message);

    @Override
    public CompletableFuture<Void> post(@NotNull Object object) {
        return CompletableFuture.runAsync(() -> {
            try {
                String message = PostedMessage.serialize(object, channel, gson);
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

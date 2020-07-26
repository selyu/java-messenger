package org.selyu.messenger.api;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public abstract class AbstractPublisher implements IPublisher {
    protected final ExecutorService executorService;
    private final Gson gson;
    private final String name;

    public AbstractPublisher(@NotNull AbstractMessageHandler channel, @NotNull String name) {
        this.executorService = channel.executorService;
        this.gson = channel.gson;
        this.name = name;
    }

    public abstract CompletableFuture<Void> postData(@NotNull String data);

    @Override
    public <T> CompletableFuture<Void> post(@NotNull T object) {
        return postData(object.getClass().getName() + "@" + gson.toJson(object) + "@" + name).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}

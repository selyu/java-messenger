package org.selyu.messaging;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public abstract class AbstractQueue implements IQueue {
    protected final ExecutorService executorService;
    private final Gson gson;
    private final String queue;

    public AbstractQueue(@NotNull AbstractChannel channel, @NotNull String queue) {
        this.executorService = channel.executorService;
        this.gson = channel.gson;
        this.queue = queue;
    }

    public abstract CompletableFuture<Void> postData(@NotNull String data);

    @Override
    public <T> CompletableFuture<Void> post(@NotNull T object) {
        return postData(object.getClass().getName() + "@" + gson.toJson(object) + "@" + queue).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}

package org.selyu.messaging;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractQueue implements IQueue {
    private final Gson gson;
    private final String queue;

    public AbstractQueue(@NotNull Gson gson, @NotNull String queue) {
        this.gson = gson;
        this.queue = queue;
    }

    public abstract <T> void postData(@NotNull String data);

    @Override
    public <T> void post(@NotNull T object) {
        postData(object.getClass().getName() + "@" + gson.toJson(object) + "@" + queue);
    }
}

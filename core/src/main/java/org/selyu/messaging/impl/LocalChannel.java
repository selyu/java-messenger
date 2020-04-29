package org.selyu.messaging.impl;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.messaging.AbstractChannel;
import org.selyu.messaging.IQueue;

public final class LocalChannel extends AbstractChannel {
    public LocalChannel(@Nullable Gson gson) {
        super(gson == null ? new Gson() : gson);
    }

    @Override
    public IQueue getQueue(@NotNull String name) {
        return queues.computeIfAbsent(name, queueName -> new LocalQueue(this, queueName));
    }
}

package org.selyu.messaging.impl;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.selyu.messaging.AbstractMessageHandler;
import org.selyu.messaging.IPublisher;

public final class LocalMessageHandler extends AbstractMessageHandler {
    public LocalMessageHandler(@Nullable Gson gson) {
        super(gson);
    }

    @Override
    public IPublisher getPublisher(@NotNull String name) {
        return publishers.computeIfAbsent(name, name1 -> new LocalPublisher(this, name));
    }
}

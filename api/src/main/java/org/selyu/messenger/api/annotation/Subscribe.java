package org.selyu.messenger.api.annotation;

import org.selyu.messenger.api.IMessageHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any method that wants to be a subscriber for the first (and only..) parameter-
 * needs to be annotated with this
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    /**
     * @return The channel to subscribe to
     */
    String value() default IMessageHandler.DEFAULT_CHANNEL;
}

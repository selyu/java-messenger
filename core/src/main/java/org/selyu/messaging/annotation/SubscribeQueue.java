package org.selyu.messaging.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation is needed for a {@link org.selyu.messaging.IChannel} to subscribe the method to the specified queue.
 * {@link SubscribeQueue#value}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubscribeQueue {
    String value() default "all";
}

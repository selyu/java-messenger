package org.selyu.messaging.annotation;

import org.selyu.messaging.IMessageHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation is needed for a {@link IMessageHandler} to subscribe the method to the specified queue.
 * {@link Subscribe#value}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    String value() default "all";
}

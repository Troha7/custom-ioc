package ua.trotsenko.di.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link Bean}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {

  String value() default "";
}

package ua.trotsenko.di.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link Component}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

  String value() default "";
}

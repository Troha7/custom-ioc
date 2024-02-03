package ua.trotsenko.di.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * {@link Configuration}
 *
 * @author Dmytro Trotsenko on 1/30/24
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

  String value() default "";
}

package ua.trotsenko.di.exception;

/**
 * {@link NoSuchBeanException}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */
public class NoSuchBeanException extends RuntimeException {

  public NoSuchBeanException(String message) {
    super(message);
  }
}

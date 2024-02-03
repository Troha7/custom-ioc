package ua.trotsenko.di.exception;

/**
 * {@link UnsupportedBeanTypeException}
 *
 * @author Dmytro Trotsenko on 1/30/24
 */
public class UnsupportedBeanTypeException extends RuntimeException{

  public UnsupportedBeanTypeException(String message) {
    super(message);
  }
}

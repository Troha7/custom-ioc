package ua.trotsenko.di.exception;

/**
 * {@link NoUniqueBeanException}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */
public class NoUniqueBeanException extends RuntimeException {

  public NoUniqueBeanException(String message) {
    super(message);
  }
}

package ua.trotsenko.di.exception;

/**
 * {@link NoUniqueBeanDefinitionException}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */
public class NoUniqueBeanDefinitionException extends RuntimeException {

  public NoUniqueBeanDefinitionException(String message) {
    super(message);
  }
}

package ua.trotsenko.di.testBean;

/**
 * {@link TestBean}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */
public interface TestBean {

  default void print() {
    System.out.println("Hello " + this.getClass().getSimpleName());
  }
}

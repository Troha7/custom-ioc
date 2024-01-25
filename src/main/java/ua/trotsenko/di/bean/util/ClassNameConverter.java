package ua.trotsenko.di.bean.util;

/**
 * {@link ClassNameConverter}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */
public class ClassNameConverter {

  public static String toBeanName(String className) {
    return className.replace(className.charAt(0), Character.toLowerCase(className.charAt(0)));
  }
}

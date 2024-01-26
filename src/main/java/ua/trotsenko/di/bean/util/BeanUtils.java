package ua.trotsenko.di.bean.util;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import ua.trotsenko.di.exception.NoSuchBeanException;
import ua.trotsenko.di.exception.NoUniqueBeanException;

/**
 * {@link BeanUtils}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */
public class BeanUtils {

  public static String toBeanName(String className) {
    return className.replace(className.charAt(0), Character.toLowerCase(className.charAt(0)));
  }

  public static <T> Collector<T, ?, T> toSingleton(Class<T> beanType) {
    return Collectors.collectingAndThen(Collectors.toList(), list -> {
      hasNoSuchBean(list, beanType);
      hasNoUniqueBean(list, beanType);
      return list.get(0);
    });
  }

  public static void hasNoUniqueBean(List<?> list, Class<?> beanType) {
    if (list.size() > 1) {
      throw new NoUniqueBeanException(
          String.format("Bean container contains: %s [%s] beans", list.size(), beanType.getName()));
    }
  }

  public static void hasNoSuchBean(List<?> list, Class<?> beanType) {
    if (list.isEmpty()) {
      throw new NoSuchBeanException(
          String.format("Bean container does not contain [%s] bean", beanType.getName()));
    }
  }
}

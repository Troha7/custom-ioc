package ua.trotsenko.di.contaxt.Impl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import ua.trotsenko.di.annotation.Inject;
import ua.trotsenko.di.annotation.Qualifier;
import ua.trotsenko.di.bean.factory.BeanFactory;
import ua.trotsenko.di.bean.factory.DefaultListableBeanFactory;
import ua.trotsenko.di.bean.scanner.BeanScanner;
import ua.trotsenko.di.bean.scanner.ComponentBeanScanner;
import ua.trotsenko.di.contaxt.ApplicationContext;
import ua.trotsenko.di.exception.NoSuchBeanException;
import ua.trotsenko.di.exception.NoUniqueBeanException;

/**
 * {@link ApplicationContextImpl}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */
public class ApplicationContextImpl implements ApplicationContext {

  private final String packageName;
  private final BeanScanner beanScanner;
  private final BeanFactory beanFactory;
  private Map<String, Object> beanContainer = new HashMap<>();

  public ApplicationContextImpl(String packageName) {
    this.packageName = packageName;
    this.beanScanner = new ComponentBeanScanner();
    this.beanFactory = new DefaultListableBeanFactory();
    createBeans();
    injectAllBeans();
  }

  @Override
  public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
    return beanContainer.values().stream()
        .filter(bean -> beanType.isAssignableFrom(bean.getClass()))
        .map(beanType::cast)
        .collect(toSingleton(beanType));
  }

  @Override
  public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException {
    var bean = beanContainer.get(name);
    if (bean != null && beanType.isAssignableFrom(bean.getClass())) {
      return beanType.cast(bean);
    }
    throw new NoSuchBeanException(
        String.format("Bean container not contains [%s] bean", beanType.getName()));
  }

  @Override
  public <T> Map<String, T> getAllBeans(Class<T> beanType) {
    return beanContainer.entrySet().stream()
        .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
        .collect(Collectors.toMap(Entry::getKey, entry -> beanType.cast(entry.getValue())));
  }

  private void createBeans() {
    beanContainer = beanFactory.createBeans(beanScanner.scan(packageName));
  }

  public <T> Collector<T, ?, T> toSingleton(Class<T> beanType) {
    return Collectors.collectingAndThen(Collectors.toList(), list ->
    {
      hasNoSuchBean(list, beanType);
      hasNoUniqueBean(list, beanType);
      return list.get(0);
    });
  }

  private void hasNoUniqueBean(List<?> list, Class<?> beanType) {
    if (list.size() > 1) {
      throw new NoUniqueBeanException(
          String.format("Bean container contains: %s [%s] beans", list.size(), beanType.getName()));
    }
  }

  private void hasNoSuchBean(List<?> list, Class<?> beanType) {
    if (list.size() < 1) {
      throw new NoSuchBeanException(
          String.format("Bean container not contains [%s] bean", beanType.getName()));
    }
  }

  private void injectAllBeans() {
    beanContainer.values()
        .forEach(bean -> Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .forEach(field -> injectBean(field, bean)));
  }

  @SneakyThrows
  private <T> void injectBean(Field field, T bean) {
    field.setAccessible(true);
    if (field.isAnnotationPresent(Qualifier.class)) {
      field.set(bean, getBean(getQualifierBeanName(field), field.getType()));
    } else {
      field.set(bean, getBean(field.getType()));
    }
  }

  private String getQualifierBeanName(Field field) {
    return field.getAnnotation(Qualifier.class).value();
  }
}

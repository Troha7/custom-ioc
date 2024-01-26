package ua.trotsenko.di.contaxt.Impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
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
  }

  @Override
  public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
    return beanFactory.getBean(beanType, beanContainer);
  }

  @Override
  public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException {
    return beanFactory.getBean(name, beanType, beanContainer);
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

}

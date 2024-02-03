package ua.trotsenko.di.contaxt.Impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import ua.trotsenko.di.bean.factory.BeanFactory;
import ua.trotsenko.di.bean.factory.DefaultListableBeanFactory;
import ua.trotsenko.di.bean.scanner.BeanScanner;
import ua.trotsenko.di.bean.scanner.ComponentBeanScanner;
import ua.trotsenko.di.bean.scanner.ConfigurationBeanScanner;
import ua.trotsenko.di.bean.util.BeanUtils;
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
  private final BeanScanner[] scanners;
  private final BeanFactory beanFactory;
  private Map<String, Object> beanContainer = new HashMap<>();

  public ApplicationContextImpl(String packageName) {
    this.packageName = packageName;
    this.scanners = new BeanScanner[]{new ComponentBeanScanner(), new ConfigurationBeanScanner()};
    this.beanFactory = new DefaultListableBeanFactory();
    createBeans();
  }

  @Override
  public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
    return beanContainer.values().stream()
        .filter(bean -> beanType.isAssignableFrom(bean.getClass()))
        .map(beanType::cast)
        .collect(BeanUtils.toSingleton(beanType));
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
    var componentNameToBeanDefinition = Arrays.stream(scanners)
        .flatMap(s -> s.scan(packageName).entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    beanContainer = beanFactory.createBeans(componentNameToBeanDefinition);
  }

}

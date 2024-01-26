package ua.trotsenko.di.bean.factory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import ua.trotsenko.di.annotation.Inject;
import ua.trotsenko.di.annotation.Qualifier;
import ua.trotsenko.di.bean.definition.BeanDefinition;
import ua.trotsenko.di.bean.util.BeanUtils;
import ua.trotsenko.di.exception.NoSuchBeanException;

/**
 * {@link DefaultListableBeanFactory}
 *
 * @author Dmytro Trotsenko on 1/21/24
 */

public class DefaultListableBeanFactory implements BeanFactory{

  @Override
  public Map<String, Object> createBeans(Map<String, BeanDefinition> beanDefinitionsMap) {

    Map<String, Object> componentBeansMap = beanDefinitionsMap.values().stream()
        .collect(Collectors.toMap(BeanDefinition::getName, this::createBean));

    injectComponentBeans(componentBeansMap);

    return componentBeansMap;
  }

  @Override
  public <T> T getBean(String name, Class<T> beanType, Map<String, Object> beansMap) {
    var bean = beansMap.get(name);
    if (bean != null && beanType.isAssignableFrom(bean.getClass())) {
      return beanType.cast(bean);
    }
    throw new NoSuchBeanException(
        String.format("Bean container not contains [%s] bean", beanType.getName()));
  }

  @Override
  public <T> T getBean(Class<T> beanType, Map<String, Object> beansMap) {
    return beansMap.values().stream()
        .filter(bean -> beanType.isAssignableFrom(bean.getClass()))
        .map(beanType::cast)
        .collect(BeanUtils.toSingleton(beanType));
  }

  @SneakyThrows
  private Object createBean(BeanDefinition beanDefinition) {
    return beanDefinition.getBeanType().getConstructor().newInstance();
  }

  private void injectComponentBeans(Map<String, Object> componentBeansMap) {
    componentBeansMap.values()
        .forEach(bean -> Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .forEach(field -> injectBean(field, bean, componentBeansMap)));
  }

  @SneakyThrows
  private <T> void injectBean(Field field, T bean, Map<String, Object> beansMap) {
    field.setAccessible(true);
    if (field.isAnnotationPresent(Qualifier.class)) {
      field.set(bean, getBean(getQualifierBeanName(field), field.getType(), beansMap));
    } else {
      field.set(bean, getBean(field.getType(), beansMap));
    }
  }

  private String getQualifierBeanName(Field field) {
    return field.getAnnotation(Qualifier.class).value();
  }

}

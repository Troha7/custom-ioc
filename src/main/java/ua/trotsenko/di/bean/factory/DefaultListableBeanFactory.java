package ua.trotsenko.di.bean.factory;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import ua.trotsenko.di.bean.definition.BeanDefinition;

/**
 * {@link DefaultListableBeanFactory}
 *
 * @author Dmytro Trotsenko on 1/21/24
 */

public class DefaultListableBeanFactory implements BeanFactory{

  @Override
  public Map<String, Object> createBeans(Map<String, BeanDefinition> beanDefinitionsMap) {
    return beanDefinitionsMap.values().stream()
        .collect(Collectors.toMap(BeanDefinition::getName, this::createBean));
  }

  @SneakyThrows
  private Object createBean(BeanDefinition beanDefinition) {
    return beanDefinition.getBeanType().getConstructor().newInstance();
  }
}

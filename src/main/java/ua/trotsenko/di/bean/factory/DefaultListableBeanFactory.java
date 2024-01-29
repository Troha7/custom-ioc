package ua.trotsenko.di.bean.factory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.SneakyThrows;
import ua.trotsenko.di.bean.definition.BeanDefinition;

/**
 * {@link DefaultListableBeanFactory}
 *
 * @author Dmytro Trotsenko on 1/21/24
 */

public class DefaultListableBeanFactory implements BeanFactory {

  private final static Map<String, Object> componentBeansMap = new HashMap<>();

  @Override
  public Map<String, Object> createBeans(Map<String, BeanDefinition> beanDefinitionsMap) {

    beanDefinitionsMap.values().stream()
        .filter(beanDefinition -> !componentBeansMap.containsKey(beanDefinition.getName()))
        .forEach(beanDefinition -> createBeanAndInjectDependencies(
            beanDefinition.getName(), beanDefinition, beanDefinitionsMap));

    return componentBeansMap;
  }

  private void createBeanAndInjectDependencies(
      String beanName,
      BeanDefinition beanDefinition,
      Map<String, BeanDefinition> beanDefinitionsMap) {

    var createdBean = createBean(beanDefinition);
    componentBeansMap.put(beanDefinition.getName(), createdBean);

    // Recursive dependency resolution
    for (Entry<String, Field> dependency : beanDefinition.getBeanDependencies().entrySet()) {
      BeanDefinition dependencyDefinition = beanDefinitionsMap.get(dependency.getKey());

      // Recursively create bean and its dependencies
      createBeanAndInjectDependencies(beanName, dependencyDefinition, beanDefinitionsMap);

      injectDependency(beanName, componentBeansMap.get(dependency.getKey()), dependency.getValue());
    }
  }

  @SneakyThrows
  private void injectDependency(String beanName, Object dependencyBean, Field field) {
    Object bean = componentBeansMap.get(beanName);
    field.setAccessible(true);
    field.set(bean, dependencyBean);
  }

  @SneakyThrows
  private Object createBean(BeanDefinition beanDefinition) {
    return beanDefinition.getBeanType().getConstructor().newInstance();
  }

}

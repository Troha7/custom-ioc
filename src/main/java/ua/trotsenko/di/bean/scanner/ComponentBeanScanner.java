package ua.trotsenko.di.bean.scanner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import ua.trotsenko.di.annotation.Component;
import ua.trotsenko.di.annotation.Inject;
import ua.trotsenko.di.annotation.Qualifier;
import ua.trotsenko.di.bean.definition.BeanDefinition;
import ua.trotsenko.di.bean.util.BeanUtils;

/**
 * {@link ComponentBeanScanner}
 *
 * @author Dmytro Trotsenko on 1/22/24
 */
public class ComponentBeanScanner implements BeanScanner {

  @Override
  public Map<String, BeanDefinition> scan(String packageName) {
    Map<String, BeanDefinition> beanDefinitionMap = new Reflections(packageName)
        .getTypesAnnotatedWith(Component.class).stream()
        .map(this::toBeanDefinition)
        .collect(Collectors.toMap(BeanDefinition::getName, beanDefinition -> beanDefinition));

    setBeanDependencies(beanDefinitionMap);

    return beanDefinitionMap;
  }

  private void setBeanDependencies(Map<String, BeanDefinition> beanDefinitionMap) {
    beanDefinitionMap.values().stream()
        .filter(beanDefinition -> Objects.nonNull(beanDefinition.getClass()))
        .forEach(beanDefinition -> addAllDependencies(beanDefinitionMap, beanDefinition));
  }

  private void addAllDependencies(Map<String, BeanDefinition> beanDefinitionMap,
      BeanDefinition beanDefinition) {
    Arrays.stream(beanDefinition.getBeanType().getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Inject.class))
        .forEach(field -> addDependency(beanDefinitionMap, beanDefinition, field));
  }

  private void addDependency(Map<String, BeanDefinition> beanDefinitionMap,
      BeanDefinition beanDefinition, Field field) {
    String beanName = BeanUtils.toBeanName(field.getType().getSimpleName());
    if (field.isAnnotationPresent(Qualifier.class)) {
      beanName = getQualifierBeanName(field);
    }
    if (!beanDefinitionMap.containsKey(beanName)) {
      beanName = getBeanName(beanDefinitionMap, field);
    }
    beanDefinition.addBeanDependency(beanName, field);
  }

  private String getBeanName(Map<String, BeanDefinition> beanDefinitionMap, Field field) {
    return beanDefinitionMap.values().stream()
        .filter(bd -> field.getType().isAssignableFrom(bd.getBeanType()))
        .collect(BeanUtils.toSingleton(BeanDefinition.class))
        .getName();
  }

  private String getQualifierBeanName(Field field) {
    return field.getAnnotation(Qualifier.class).value();
  }

  private BeanDefinition toBeanDefinition(Class<?> beanType) {
    String beanName = beanType.getAnnotation(Component.class).value();
    String defaultBeanName = BeanUtils.toBeanName(beanType.getSimpleName());
    beanName = beanName.isEmpty() ? defaultBeanName : beanName;
    return new BeanDefinition(beanName, beanType);
  }
}

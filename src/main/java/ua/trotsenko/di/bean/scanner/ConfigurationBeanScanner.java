package ua.trotsenko.di.bean.scanner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.reflections.Reflections;
import ua.trotsenko.di.annotation.Bean;
import ua.trotsenko.di.annotation.Configuration;
import ua.trotsenko.di.bean.definition.BeanDefinition;
import ua.trotsenko.di.bean.util.BeanUtils;
import ua.trotsenko.di.exception.NoUniqueBeanDefinitionException;
import ua.trotsenko.di.exception.UnsupportedBeanTypeException;

/**
 * {@link ConfigurationBeanScanner}
 *
 * @author Dmytro Trotsenko on 1/30/24
 */
public class ConfigurationBeanScanner implements BeanScanner {

  @Override
  public Map<String, BeanDefinition> scan(String packageName) {

    Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    new Reflections(packageName)
        .getTypesAnnotatedWith(Configuration.class).stream()
        .map(this::toBeanDefinition)
        .peek(configDefinition -> addBeanDefinitionToMap(beanDefinitionMap, configDefinition))
        .forEach(configDefinition -> {
          if (beanDefinitionMap.containsKey(configDefinition.getName())) {
            throw new NoUniqueBeanDefinitionException(
                String.format("Duplicate bean [%s]. Check duplicates name in @Component or @Bean",
                    configDefinition.getName()));
          }
          beanDefinitionMap.put(configDefinition.getName(), configDefinition);
          addAllDependenciesToConfigBean(beanDefinitionMap, configDefinition);
          addAllDependenciesToBean(beanDefinitionMap, configDefinition);
        });

    return beanDefinitionMap;
  }

  private void addBeanDefinitionToMap(Map<String, BeanDefinition> beanDefinitionMap,
      BeanDefinition configDefinition) {

    Arrays.stream(configDefinition.getBeanType().getMethods())
        .filter(method -> method.isAnnotationPresent(Bean.class))
        .map(this::toBeanDefinition)
        .forEach(beanDefinition -> beanDefinitionMap.put(beanDefinition.getName(), beanDefinition));
  }

  private BeanDefinition toBeanDefinition(Class<?> beanType) {
    String beanName = beanType.getAnnotation(Configuration.class).value();
    String defaultBeanName = BeanUtils.toBeanName(beanType.getSimpleName());
    beanName = beanName.isEmpty() ? defaultBeanName : beanName;
    return new BeanDefinition(beanName, beanType);
  }

  private BeanDefinition toBeanDefinition(Method method) {
    var beanType = method.getReturnType();
    if (beanType.equals(Void.TYPE)) {
      throw new UnsupportedBeanTypeException
          ("Configuration bean with the return type void could not be created");
    }
    String beanName = method.getAnnotation(Bean.class).value();
    String defaultBeanName = BeanUtils.toBeanName(beanType.getSimpleName());
    beanName = beanName.isEmpty() ? defaultBeanName : beanName;
    return new BeanDefinition(beanName, beanType);
  }

  private void addAllDependenciesToConfigBean(Map<String, BeanDefinition> beanDefinitionMap,
      BeanDefinition beanDefinition) {
    Arrays.stream(beanDefinition.getBeanType().getMethods())
        .filter(method -> method.isAnnotationPresent(Bean.class))
        .forEach(method -> beanDefinition.addBeanDependency(getBeanName(beanDefinitionMap, method),
            method));
  }

  private void addAllDependenciesToBean(Map<String, BeanDefinition> beanDefinitionMap,
      BeanDefinition configDefinition) {
    Map<String, Object> configBeanDependencies = configDefinition.getBeanDependencies();
    configBeanDependencies.entrySet().stream()
        .filter(beanDependency -> beanDependency.getValue() instanceof Method)
        .forEach(beanDependency -> addDependencyToBean(beanDefinitionMap, beanDependency));
  }

  private void addDependencyToBean(Map<String, BeanDefinition> beanDefinitionMap,
      Entry<String, Object> beanDependency) {
    var beanDefinition = beanDefinitionMap.get(beanDependency.getKey());
    Method method = (Method) beanDependency.getValue();
    Arrays.stream(method.getParameterTypes())
        .forEach(paramType -> {
          String beanName = getBeanDefinition(beanDefinitionMap, paramType).getName();
          beanDefinition.addBeanDependency(beanName, method.getParameterCount());
        });
  }

  private String getBeanName(Map<String, BeanDefinition> beanDefinitionMap, Method method) {
    String beanName = method.getAnnotation(Bean.class).value();
    if (beanName.isEmpty()) {
      return BeanUtils.toBeanName(method.getReturnType().getSimpleName());
    }
    if (!beanDefinitionMap.containsKey(beanName)) {
      return getBeanDefinition(beanDefinitionMap, method.getReturnType()).getName();
    }
    return beanName;
  }

  private BeanDefinition getBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap,
      Class<?> beanType) {
    return beanDefinitionMap.values().stream()
        .filter(bd -> beanType.isAssignableFrom(bd.getBeanType()))
        .collect(BeanUtils.toSingleton(BeanDefinition.class));
  }

}

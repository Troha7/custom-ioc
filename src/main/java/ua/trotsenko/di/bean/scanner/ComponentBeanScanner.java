package ua.trotsenko.di.bean.scanner;

import java.util.Map;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import ua.trotsenko.di.annotation.Component;
import ua.trotsenko.di.bean.definition.BeanDefinition;
import ua.trotsenko.di.bean.util.ClassNameConverter;

/**
 * {@link ComponentBeanScanner}
 *
 * @author Dmytro Trotsenko on 1/22/24
 */
public class ComponentBeanScanner implements BeanScanner {

  @Override
  public Map<String, BeanDefinition> scan(String packageName) {
    return new Reflections(packageName).getTypesAnnotatedWith(Component.class).stream()
        .map(this::toBeanDefinition)
        .collect(Collectors.toMap(BeanDefinition::getName, beanDefinition -> beanDefinition));
  }

  private BeanDefinition toBeanDefinition(Class<?> beanType) {
    String beanName = beanType.getAnnotation(Component.class).value();
    String defaultBeanName = ClassNameConverter.toBeanName(beanType.getSimpleName());
    beanName = beanName.isEmpty() ? defaultBeanName : beanName;
    return new BeanDefinition(beanName, beanType);
  }
}

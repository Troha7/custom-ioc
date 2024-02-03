package ua.trotsenko.di.bean.definition;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * {@link BeanDefinition}
 *
 * @author Dmytro Trotsenko on 1/21/24
 */

@RequiredArgsConstructor
@Getter
@Setter
public class BeanDefinition {

  private final String name;
  private final Class<?> beanType;
  private final Map<String, Object> beanDependencies = new HashMap<>();

  public <T> void addBeanDependency(String beanName, T dependentObject) {
    this.beanDependencies.put(beanName, dependentObject);
  }

}

package ua.trotsenko.di.bean.definition;

import java.lang.reflect.Field;
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
  private final Map<String, Field> beanDependencies = new HashMap<>();

  public void addBeanDependency(String beanName, Field field){
    this.beanDependencies.put(beanName, field);
  }

}

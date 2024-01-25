package ua.trotsenko.di.bean.definition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * {@link BeanDefinition}
 *
 * @author Dmytro Trotsenko on 1/21/24
 */

@RequiredArgsConstructor
@Getter
public class BeanDefinition {

  private final String name;
  private final Class<?> beanType;

}

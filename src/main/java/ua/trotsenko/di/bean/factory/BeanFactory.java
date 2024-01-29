package ua.trotsenko.di.bean.factory;

import java.util.Map;
import ua.trotsenko.di.bean.definition.BeanDefinition;

/**
 * {@link BeanFactory}
 *
 * @author Dmytro Trotsenko on 1/21/24
 */
public interface BeanFactory {

  Map<String, Object> createBeans(Map<String, BeanDefinition> beanDefinitionsMap);
}

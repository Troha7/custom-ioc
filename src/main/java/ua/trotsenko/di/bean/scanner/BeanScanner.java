package ua.trotsenko.di.bean.scanner;

import java.util.Map;
import ua.trotsenko.di.bean.definition.BeanDefinition;

/**
 * {@link BeanScanner}
 *
 * @author Dmytro Trotsenko on 1/22/24
 */
public interface BeanScanner {

  Map<String, BeanDefinition> scan(String packageName);

}

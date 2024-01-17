package ua.trotsenko.di.contaxt;

import java.util.Map;
import ua.trotsenko.di.exception.NoSuchBeanException;
import ua.trotsenko.di.exception.NoUniqueBeanException;

/**
 * {@link ApplicationContext}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */
public interface ApplicationContext {

  <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

  <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException;

  <T> Map<String, T> getAllBeans(Class<T> beanType);
}

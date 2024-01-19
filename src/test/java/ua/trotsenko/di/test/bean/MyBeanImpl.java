package ua.trotsenko.di.test.bean;

import lombok.Getter;
import ua.trotsenko.di.annotation.Bean;
import ua.trotsenko.di.annotation.Inject;

/**
 * {@link MyBeanImpl}
 *
 * @author Dmytro Trotsenko on 1/18/24
 */

@Bean("test")
@Getter
public class MyBeanImpl implements MyBean {

  @Inject
  private TestBean1Impl testBean1;
}

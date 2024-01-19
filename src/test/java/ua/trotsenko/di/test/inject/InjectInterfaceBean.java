package ua.trotsenko.di.test.inject;

import ua.trotsenko.di.annotation.Bean;
import ua.trotsenko.di.annotation.Inject;
import ua.trotsenko.di.test.bean.TestBean;

/**
 * {@link InjectInterfaceBean}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */

@Bean
public class InjectInterfaceBean {

  @Inject
  private TestBean testBean1;
}

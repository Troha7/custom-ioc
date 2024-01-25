package ua.trotsenko.di.test.inject;

import ua.trotsenko.di.annotation.Component;
import ua.trotsenko.di.annotation.Inject;
import ua.trotsenko.di.test.bean.TestBean;

/**
 * {@link InjectInterfaceBean}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */

@Component
public class InjectInterfaceBean {

  @Inject
  private TestBean testBean1;
}

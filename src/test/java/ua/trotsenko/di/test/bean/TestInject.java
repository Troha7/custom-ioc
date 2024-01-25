package ua.trotsenko.di.test.bean;

import lombok.Getter;
import lombok.ToString;
import ua.trotsenko.di.annotation.Component;
import ua.trotsenko.di.annotation.Inject;
import ua.trotsenko.di.annotation.Qualifier;

/**
 * {@link TestInject}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */

@Component
@ToString
@Getter
public class TestInject {

  @Inject
  @Qualifier("testBean1Impl")
  private TestBean testBean1;
  @Inject
  private TestBean2Impl testBean2;
}

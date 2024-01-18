package ua.trotsenko.di.testBean;

import lombok.Getter;
import lombok.ToString;
import ua.trotsenko.di.annotation.Bean;
import ua.trotsenko.di.annotation.Inject;
import ua.trotsenko.di.annotation.Qualifier;

/**
 * {@link TestInject}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */

@Bean
@ToString
@Getter
public class TestInject {

  @Inject
  @Qualifier("testBean1Impl")
  private TestBean testBean1;
  @Inject
  private TestBean2Impl testBean2;
}

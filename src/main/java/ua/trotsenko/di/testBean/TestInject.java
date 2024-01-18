package ua.trotsenko.di.testBean;

import lombok.Getter;
import lombok.ToString;
import ua.trotsenko.di.annotation.Bean;
import ua.trotsenko.di.annotation.Inject;

/**
 * {@link TestInject}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */

@Bean
@ToString
@Getter
public class TestInject{

  @Inject
  private TestBean1Impl testBean1;
  @Inject
  private TestBean2Impl testBean2;
}

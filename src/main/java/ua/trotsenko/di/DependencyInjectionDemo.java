package ua.trotsenko.di;

import ua.trotsenko.di.contaxt.Impl.ApplicationContextImpl;
import ua.trotsenko.di.testBean.TestBean;

/**
 * {@link DependencyInjectionDemo}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */
public class DependencyInjectionDemo {

  public static void main(String[] args) {
    var context = new ApplicationContextImpl("ua.trotsenko.di.testBean");
    var testBean = context.getBean("testBean1Impl", TestBean.class);
    testBean.print();

    System.out.println(context.getAllBeans(TestBean.class));
  }
}

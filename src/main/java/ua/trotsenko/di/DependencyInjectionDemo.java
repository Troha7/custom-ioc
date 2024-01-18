package ua.trotsenko.di;

import ua.trotsenko.di.contaxt.Impl.ApplicationContextImpl;
import ua.trotsenko.di.testBean.TestBean;
import ua.trotsenko.di.testBean.TestInject;

/**
 * {@link DependencyInjectionDemo}
 *
 * @author Dmytro Trotsenko on 1/17/24
 */
public class DependencyInjectionDemo {

  public static void main(String[] args) {
    var context = new ApplicationContextImpl("ua.trotsenko.di.testBean");
    var testBean = context.getBean(TestInject.class);
    testBean.getTestBean1().print();
    testBean.getTestBean2().print();

    System.out.println(context.getAllBeans(TestBean.class));
  }
}
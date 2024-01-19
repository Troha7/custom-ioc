package ua.trotsenko.di.context;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.trotsenko.di.contaxt.ApplicationContext;
import ua.trotsenko.di.contaxt.Impl.ApplicationContextImpl;
import ua.trotsenko.di.exception.NoSuchBeanException;
import ua.trotsenko.di.exception.NoUniqueBeanException;
import ua.trotsenko.di.test.NotScannedBean;
import ua.trotsenko.di.test.bean.MyBean;
import ua.trotsenko.di.test.bean.MyBeanImpl;
import ua.trotsenko.di.test.bean.NotBean;
import ua.trotsenko.di.test.bean.TestBean;
import ua.trotsenko.di.test.bean.TestBean1Impl;
import ua.trotsenko.di.test.bean.TestBean2Impl;
import ua.trotsenko.di.test.bean.TestInject;

/**
 * {@link ApplicationContextTest}
 *
 * @author Dmytro Trotsenko on 1/18/24
 */
public class ApplicationContextTest {

  private ApplicationContext context;

  @BeforeEach
  void setUp() {
    context = new ApplicationContextImpl("ua.trotsenko.di.test.bean");
  }

  @Test
  public void getBean_shouldReturnInstanceClassByClassType() {
    // When
    var myBean = context.getBean(MyBean.class);
    var testBean1 = context.getBean(TestBean1Impl.class);

    // Then
    assertInstanceOf(MyBeanImpl.class, myBean);
    assertInstanceOf(TestBean1Impl.class, testBean1);
  }

  @Test
  public void getBean_shouldReturnInstanceClassByInterfaceClassType() {
    // When
    var myBean = context.getBean(MyBeanImpl.class);

    // Then
    assertInstanceOf(MyBeanImpl.class, myBean);
  }

  @Test
  public void getBean_shouldTrowNoSuchBeanExceptionWhenGetBeanByClassType() {
    assertThrows(NoSuchBeanException.class, () -> context.getBean(NotScannedBean.class));
    assertThrows(NoSuchBeanException.class, () -> context.getBean(NotBean.class));
  }

  @Test
  public void getBean_shouldTrowNotUniqueBeanException() {
    assertThrows(NoUniqueBeanException.class, () -> context.getBean(TestBean.class));
  }

  @Test
  public void getBean_shouldReturnInstanceClassByBeanNameAndClassType() {
    // When
    var myBean = context.getBean("test", MyBeanImpl.class);
    var testBean1Impl = context.getBean("testBean1Impl", TestBean1Impl.class);

    // Then
    assertInstanceOf(MyBeanImpl.class, myBean);
    assertInstanceOf(TestBean1Impl.class, testBean1Impl);
  }

  @Test
  public void getBean_shouldReturnInstanceClassByBeanNameAndInterfaceClassType() {
    // When
    var myBean = context.getBean("test", MyBean.class);
    var testBean1 = context.getBean("testBean1Impl", TestBean.class);

    // Then
    assertInstanceOf(MyBeanImpl.class, myBean);
    assertInstanceOf(TestBean1Impl.class, testBean1);
  }

  @Test
  public void getBean_shouldTrowNoSuchBeanExceptionWhenGetBeanByBeanNameAndClassType() {
    assertThrows(NoSuchBeanException.class, () -> context.getBean("notExists", MyBeanImpl.class));
  }

  @Test
  public void getAllBeans_shouldReturnMapContainsBeanNamesAndBeanObjects() {
    // When
    var beans = context.getAllBeans(TestBean.class);
    var testBean1Impl = beans.get("testBean1Impl");
    var testBean2Impl = beans.get("testBean2Impl");

    // Then
    assertEquals(2, beans.size());
    assertInstanceOf(TestBean1Impl.class, testBean1Impl);
    assertInstanceOf(TestBean2Impl.class, testBean2Impl);
  }

  @Test
  public void getBean_shouldInjectedBeanToClassField() {
    // When
    var myBean = context.getBean(MyBeanImpl.class);
    var testBean1 = myBean.getTestBean1();

    // Then
    assertInstanceOf(TestBean1Impl.class, testBean1);
  }

  @Test
  public void getBean_shouldTrowNotUniqueBeanExceptionWhenTryInjectInterface() {
    assertThrows(NoUniqueBeanException.class,
        () -> new ApplicationContextImpl("ua.trotsenko.di.test"));
  }

  @Test
  public void getBean_shouldInjectedBeanWhenQualifyClassField() {
    // When
    var testInject = context.getBean(TestInject.class);
    var testBean1 = testInject.getTestBean1();
    var testBean2 = testInject.getTestBean2();

    // Then
    assertInstanceOf(TestInject.class, testInject);
    assertInstanceOf(TestBean1Impl.class, testBean1);
    assertInstanceOf(TestBean2Impl.class, testBean2);
  }
}

package ua.trotsenko.di.test;

import ua.trotsenko.di.annotation.Bean;
import ua.trotsenko.di.test.bean.MyBean;

/**
 * {@link NotScannedBean}
 *
 * @author Dmytro Trotsenko on 1/18/24
 */

@Bean
public class NotScannedBean implements MyBean {

}

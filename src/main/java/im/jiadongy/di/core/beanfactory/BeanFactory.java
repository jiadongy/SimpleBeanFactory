package im.jiadongy.di.core.beanfactory;

/**
 * Created by jiadongy on 16/7/8.
 */
public interface BeanFactory {

    <T> T getBean(String beanId);

    <T> T getBean(Class<?> clazz);

}

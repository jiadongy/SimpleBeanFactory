package im.jiadongy.di.core.beanfactory;

/**
 * Created by jiadongy on 16/7/8.
 */
public interface BeanFactory {

    <T> T getBean(String beanId, Class<T> requiredType) throws Exception;

    Object getBean(String beanId) throws Exception;

    <T> T getBean(Class<T> clazz) throws Exception;

}

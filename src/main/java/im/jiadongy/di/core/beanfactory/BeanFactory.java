package im.jiadongy.di.core.beanfactory;

import im.jiadongy.di.core.definition.BeanDefinition;

import java.util.Set;

/**
 * Created by jiadongy on 16/7/8.
 */
public interface BeanFactory {

    <T> T getBean(String beanId);

    <T> T getBean(Class<?> clazz);

    BeanDefinition getBeanDefinitionById(String beanId);

    Set<BeanDefinition> getBeanDefinitionByClass(String clazz);
}

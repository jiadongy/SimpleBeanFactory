package im.jiadongy.di.core.beanfactory;

import im.jiadongy.di.core.definition.BeanDefinition;

import java.util.Set;

/**
 * Created by jiadongy on 16/7/10.
 */
public interface BeanDefinitionManager {

    BeanDefinition getBeanDefinitionById(String beanId);

    Set<BeanDefinition> getAllBeanDefinitions();
}

package im.jiadongy.di.core.beanfactory;

import im.jiadongy.di.xml.BeanDefinitionXMLParser;

import java.util.List;

/**
 * Created by jiadongy on 16/7/8.
 */
public class ListableBeanFactoryImpl extends AbstractBeanFactory implements ListableBeanFactory {



    private BeanDefinitionXMLParser parser = new BeanDefinitionXMLParser(this);

    public <T> List<T> getAllBeans() {
        return null;
    }


}

package im.jiadongy.di.core.beanfactory;

import im.jiadongy.di.core.definition.BeanWrapper;
import im.jiadongy.di.xml.BeanDefinitionXMLParser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiadongy on 16/7/8.
 */
public class ListableBeanFactoryImpl extends AbstractBeanFactory implements ListableBeanFactory {



    private BeanDefinitionXMLParser parser = new BeanDefinitionXMLParser(this);


    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getAllBeans(Class<T> clazz) {
        return beanMap.values().stream().map(wrapper -> (T) wrapper.getObj()).collect(Collectors.toList());
    }

    @Override
    public List<Object> getAllBeans() {
        return beanMap.values().stream().map(BeanWrapper::getObj).collect(Collectors.toList());
    }
}

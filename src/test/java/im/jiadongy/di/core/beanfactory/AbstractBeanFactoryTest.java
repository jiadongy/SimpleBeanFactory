package im.jiadongy.di.core.beanfactory;

import im.jiadongy.di.xml.BeanDefinitionXMLParser;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by jiadongy on 16/7/10.
 */
public class AbstractBeanFactoryTest {

    ListableBeanFactoryImpl listableBeanFactory = new ListableBeanFactoryImpl();

    @Test
    public void startCreateBeans() throws Exception {
        listableBeanFactory.loadBeanDefinition(new BeanDefinitionXMLParser(listableBeanFactory), new File("/Users/jiadongy/simple-di-framework/src/test/java/testdi/appcontext-bean.xml"));
        listableBeanFactory.startCreateBeans(listableBeanFactory.getAllBeanDefinitions());
        List<Object> objectList = listableBeanFactory.getAllBeans();
    }

    @Test
    public void createBean() throws Exception {

    }

}
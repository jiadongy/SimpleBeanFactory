package im.jiadongy.di.core.beanfactory;

import im.jiadongy.di.xml.BeanDefinitionXMLParser;
import org.junit.Test;

import java.io.File;

/**
 * Created by jiadongy on 16/7/9.
 */
public class ListableBeanFactoryImplTest {


    ListableBeanFactoryImpl beanFactory = new ListableBeanFactoryImpl();

    @Test
    public void parse() throws Exception {
        beanFactory.loadBeanDefinition(new BeanDefinitionXMLParser(beanFactory), new File("/Users/jiadongy/simple-di-framework/src/main/resources/spring/appcontext-bean.xml"));
    }

}
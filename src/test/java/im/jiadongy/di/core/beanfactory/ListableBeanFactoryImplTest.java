package im.jiadongy.di.core.beanfactory;

import org.junit.Test;

import java.io.File;

/**
 * Created by jiadongy on 16/7/9.
 */
public class ListableBeanFactoryImplTest {


    ListableBeanFactoryImpl beanFactory = new ListableBeanFactoryImpl();

    @Test
    public void parse() throws Exception {
        beanFactory.parse(new File("/Users/jiadongy/simple-di-framework/src/main/resources/spring/appcontext-bean.xml"));
    }

}
package im.jiadongy.di.core.beanfactory;

import java.util.List;

/**
 * Created by jiadongy on 16/7/8.
 */
public interface ListableBeanFactory extends BeanFactory {

    <T> List<T> getBeans();
}

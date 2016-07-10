package im.jiadongy.di.core.definition;

/**
 * Created by jiadongy on 16/7/10.
 */
public class BeanWrapper {

    Object obj;

    Class<?> clazz;

    BeanDefinition definition;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public BeanDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(BeanDefinition definition) {
        this.definition = definition;
    }
}

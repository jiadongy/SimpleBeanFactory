package im.jiadongy.di.core.definition;

/**
 * Created by jiadongy on 16/7/9.
 */
public class ConstructorArgs {

    private boolean isBean;
    private String value;
    private String ref;
    private BeanDefinition definition;

    public BeanDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(BeanDefinition definition) {
        this.definition = definition;
    }

    public boolean isBean() {
        return isBean;
    }

    public void setBean(boolean bean) {
        isBean = bean;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}

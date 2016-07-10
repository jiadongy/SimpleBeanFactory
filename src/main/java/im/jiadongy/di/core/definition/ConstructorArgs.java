package im.jiadongy.di.core.definition;

/**
 * Created by jiadongy on 16/7/9.
 */
public class ConstructorArgs {

    public static final int VALUE = 0;
    public static final int PROPERTY = 1;
    public static final int BEAN_REF = 2;
    public static final int BEAN_DEFINITION = 3;


    private int type = VALUE;
    private String value;
    private String ref;
    private BeanDefinition definition;
    private Property property;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public BeanDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(BeanDefinition definition) {
        this.definition = definition;
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

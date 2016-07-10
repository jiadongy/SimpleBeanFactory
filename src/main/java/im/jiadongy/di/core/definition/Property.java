package im.jiadongy.di.core.definition;

/**
 * Created by jiadongy on 16/7/9.
 */
public class Property {

    public static final int VALUE = 0;
    public static final int BEAN_REF = 1;
    public static final int BEAN_DEFINITION = 2;

    private String name;

    private int type;

    private String ref;

    private String value;

    private BeanDefinition definition;

    public BeanDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(BeanDefinition definition) {
        this.definition = definition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

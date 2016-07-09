package im.jiadongy.di.core.definition;

/**
 * Created by jiadongy on 16/7/9.
 */
public class Property {

    private boolean isBean;

    private String name;

    private String ref;

    private String value;

    public boolean isBean() {
        return isBean;
    }

    public void setBean(boolean bean) {
        isBean = bean;
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

}

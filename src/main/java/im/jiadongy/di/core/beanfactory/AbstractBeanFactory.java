package im.jiadongy.di.core.beanfactory;

import im.jiadongy.di.core.definition.BeanDefinition;
import im.jiadongy.di.core.definition.BeanWrapper;
import im.jiadongy.di.core.definition.ConstructorArgs;
import im.jiadongy.di.core.definition.Property;
import im.jiadongy.di.core.exception.SimpleDIException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static im.jiadongy.di.core.definition.ConstructorArgs.BEAN_DEFINITION;
import static im.jiadongy.di.core.definition.ConstructorArgs.BEAN_REF;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by jiadongy on 16/7/10.
 */
abstract public class AbstractBeanFactory extends AbstractBeanDefinitionManager implements BeanFactory {

    protected Map<String, BeanWrapper> beanMap = new ConcurrentHashMap<>();
    protected Map<String, Set<BeanWrapper>> beanTypeMap = new ConcurrentHashMap<>();

    private Set<BeanDefinition> createInProgress = new HashSet<>();
    private Set<BeanDefinition> createFinished = new HashSet<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(String beanId, Class<T> requiredType) throws Exception {
        return (T) beanMap.get(beanId).getObj();
    }

    @Override
    public Object getBean(String beanId) throws Exception {
        return beanMap.get(beanId).getObj();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> clazz) throws Exception {
        Set<BeanWrapper> wrappers = beanTypeMap.get(clazz.getName());
        if (wrappers.size() != 1)
            throw new SimpleDIException("get more than one bean by class " + clazz.getName());
        return (T) wrappers.iterator().next().getObj();
    }

    protected void startCreateBeans(Collection<BeanDefinition> list) throws Exception {
        for (BeanDefinition definition : list) {
            BeanWrapper wrapper = createBean(definition);

            beanMap.putIfAbsent(wrapper.getDefinition().getId(), wrapper);
            if (!beanTypeMap.containsKey(wrapper.getClazz().getName()))
                beanTypeMap.putIfAbsent(wrapper.getClazz().getName(), new HashSet<>());
            beanTypeMap.get(wrapper.getClazz().getName()).add(wrapper);

        }
    }

    private BeanWrapper createBean(BeanDefinition definition) throws Exception {

        boolean isIdBlank = isBlank(definition.getId()),
                isClazzBlank = isBlank(definition.getClazz()),
                isFactoryBeanBlank = isBlank(definition.getFactoryBean()),
                isFactoryMethodBlank = isBlank(definition.getFactoryMethod()),
                isInitMethodBlank = isBlank(definition.getInitMethod());

        if (createFinished.contains(definition) && !isIdBlank
                && beanMap.containsKey(definition.getId()))
            return beanMap.get(definition.getId());

        if (createInProgress.contains(definition))
            throw new SimpleDIException("creator : circular dependency detected");

        createInProgress.add(definition);

        if (isClazzBlank && isFactoryBeanBlank)
            throw new SimpleDIException("creator : class or factoryBean missing");
        else if (!isClazzBlank && !isFactoryBeanBlank)
            throw new SimpleDIException("creator : class or factoryBean duplicate");

        BeanWrapper wrapper = new BeanWrapper();
        wrapper.setDefinition(definition);

        List<ConstructorArgs> parameters = definition.getConstructorArgs();

        //Factory
        if (!isFactoryMethodBlank) {
            Object factory = isFactoryBeanBlank ?
                    Class.forName(definition.getClazz()).newInstance() :
                    createBean(getBeanDefinitionById(definition.getFactoryBean())).getObj();

            List<Method> factoryMethods = Arrays.asList(factory.getClass().getMethods()).stream().
                    filter(method -> method.getName().equals(definition.getFactoryMethod())
                            && method.getParameterCount() == parameters.size()).collect(Collectors.toList());

            if (factoryMethods.isEmpty())
                throw new SimpleDIException("creator : can't find specified factory method with specified parameter count");

            else {
                if (!factoryMethods.stream().anyMatch(method -> {
                    try {
                        List<Object> realParameters = parseActualConstructorParameters(method.getParameterTypes(), parameters);
                        //papi java.lang.IllegalArgumentException: argument type mismatch : 接受一个数组,传入list就错了!!
                        wrapper.setObj(method.invoke(factory, realParameters.toArray()));
                        wrapper.setClazz(method.getReturnType());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }))
                    throw new SimpleDIException("creator : can't find factory method with type matching");


            }
        }
        //Constructor
        else {
            List<Constructor> constructors = Arrays.asList(Class.forName(definition.getClazz()).getDeclaredConstructors()).stream().
                    filter(constructor -> constructor.getParameterCount() == parameters.size()).collect(Collectors.toList());

            if (constructors.isEmpty())
                throw new SimpleDIException("creator : can't find specified public constructor method with specified parameter count");

            if (!constructors.stream().anyMatch(constructor -> {
                try {
                    List<Object> realParameters = parseActualConstructorParameters(constructor.getParameterTypes(), parameters);
                    //papi constructor.newInstance(args) args size 一定要 > 0 否则exception ??
                    wrapper.setObj(constructor.newInstance(realParameters.toArray()));
                    wrapper.setClazz(constructor.getDeclaringClass());
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }))
                throw new SimpleDIException("creator : can't find constructor method with type matching");
        }

        if (wrapper.getObj() == null || wrapper.getClazz() == null || wrapper.getDefinition() == null)
            throw new SimpleDIException("creator : beanWrapper invalid");

        //todo populate
        populateBean(wrapper, definition.getProperties());

        //todo init
        if (!isInitMethodBlank)
            wrapper.getClazz().getDeclaredMethod(definition.getInitMethod()).invoke(wrapper.getObj());

        createInProgress.remove(definition);
        createFinished.add(definition);
        return wrapper;

    }

    private void populateBean(BeanWrapper wrapper, List<Property> properties) throws SimpleDIException {
        Class<?> clazz = wrapper.getClazz();
        if (!properties.stream().allMatch(property -> {
            try {
                if (clazz.isInstance(wrapper.getObj())) {
                    //papi primitiveType.getDeclaredField可以, primitiveType.getField报NoSuchFieldException ?? //pipa getField只能获得public字段!!
                    Field field = clazz.getDeclaredField(property.getName());
                    field.setAccessible(true);
                    //pipa Field.getDeclaringClass 获得Field所在实例的class,用getType获得其基本类型的class
                    field.set(wrapper.getObj(),
                            property.getType() == Property.BEAN_DEFINITION ?
                                    createBean(wrapper.getDefinition()).getObj() :
                                    parseParameter(property.getType() == Property.BEAN_REF,
                                            property.getType() == Property.BEAN_REF ? property.getRef() : property.getValue(),
                                            field.getType()));
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }))
            throw new SimpleDIException("populate : some fields populate fail");
    }


    private List<Object> parseActualConstructorParameters(Class<?>[] parameterTypes, List<ConstructorArgs> constructorArgsList) throws Exception {

        if (parameterTypes.length != constructorArgsList.size())
            throw new SimpleDIException("creator : size of parameters not equal to size of constructorArgs");

        List<Object> res = new ArrayList<>(parameterTypes.length);

        for (int i = 0; i < parameterTypes.length; i++) {
            ConstructorArgs args = constructorArgsList.get(i);
            int type = args.getType();
            String refOrValue;
            if (type == BEAN_REF)
                refOrValue = args.getRef();
            else
                refOrValue = args.getValue();

            res.add(type == BEAN_DEFINITION ?
                    createBean(args.getDefinition()).getObj() :
                    parseParameter(type == BEAN_REF, refOrValue, parameterTypes[i])
            );
        }

        return res;
    }


    private java.io.Serializable parsePrimitiveParameter(String s, Class<?> primitiveType) throws SimpleDIException {
        try {
            //pipa primitiveType == Integer.class是不行的,因为Field只能获得type也就是其基本类型"int","boolean"等
            if (primitiveType == int.class)
                return Integer.parseInt(s);
            else if (primitiveType == short.class)
                return Short.parseShort(s);
            else if (primitiveType == long.class)
                return Long.parseLong(s);
            else if (primitiveType == byte.class)
                return Byte.parseByte(s);
            else if (primitiveType == boolean.class)
                return Boolean.parseBoolean(s);
            else if (primitiveType == float.class)
                return Float.parseFloat(s);
            else if (primitiveType == double.class)
                return Double.parseDouble(s);
            else if (primitiveType == char.class)
                return s.charAt(0);
        } catch (Exception e) {
            throw new SimpleDIException("parsePrimitive fail with " + s + " -> " + primitiveType.getName(), e);
        }

        return s;
    }

    private Object parseParameter(boolean isBean, String refOrValue, Class<?> clazz) throws Exception {
        if (isBean) {
            BeanWrapper res = createBean(getBeanDefinitionById(refOrValue));
            if (res.getClazz() == clazz)
                return res.getObj();
            else
                throw new SimpleDIException("creator : type of bean created not match primitiveType " + clazz.getName());
        } else
            return parsePrimitiveParameter(refOrValue, clazz);
    }


}

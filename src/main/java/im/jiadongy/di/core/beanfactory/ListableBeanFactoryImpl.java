package im.jiadongy.di.core.beanfactory;

import im.jiadongy.di.core.definition.BeanDefinition;
import im.jiadongy.di.core.exception.SimpleDIException;
import im.jiadongy.di.xml.BeanDefinitionXMLParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiadongy on 16/7/8.
 */
public class ListableBeanFactoryImpl implements ListableBeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private Map<String, Set<BeanDefinition>> beanDefinitionTypeMap = new ConcurrentHashMap<>();

    private BeanDefinitionXMLParser parser = new BeanDefinitionXMLParser(this);

    public <T> List<T> getBeans() {
        return null;
    }

    public <T> T getBean(String beanName) {
        return null;
    }

    public <T> T getBean(Class<?> clazz) {
        return null;
    }

    public BeanDefinition getBeanDefinitionById(String beanId) {
        return beanDefinitionMap.get(beanId);
    }

    public Set<BeanDefinition> getBeanDefinitionByClass(String clazz) {
        return beanDefinitionTypeMap.get(clazz);
    }

    public void parse(File xml) throws SAXException, SimpleDIException, ParserConfigurationException, IOException {
        if (xml.isFile() && xml.exists()) {
            Set<BeanDefinition> result = parser.parse(xml);
            for (BeanDefinition definition : result) {
                beanDefinitionMap.putIfAbsent(definition.getId(), definition);
            }

            for (String id : beanDefinitionMap.keySet()) {
                String type = beanDefinitionMap.get(id).getClazz();
                if (!beanDefinitionTypeMap.containsKey(type))
                    beanDefinitionTypeMap.put(type, new HashSet<>());

                beanDefinitionTypeMap.get(type).add(beanDefinitionMap.get(id));
            }
        }
    }
}

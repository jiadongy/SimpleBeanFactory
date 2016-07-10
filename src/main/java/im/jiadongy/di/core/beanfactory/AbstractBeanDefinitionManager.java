package im.jiadongy.di.core.beanfactory;

import im.jiadongy.di.core.definition.BeanDefinition;
import im.jiadongy.di.xml.BeanDefinitionParser;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by jiadongy on 16/7/10.
 */
abstract public class AbstractBeanDefinitionManager implements BeanDefinitionManager {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    @Override
    public BeanDefinition getBeanDefinitionById(String beanId) {
        //pipa ConcurrentHashMap 不允许传入null,会NPE,HashMap可以
        return beanDefinitionMap.get(beanId);
    }

    @Override
    public Set<BeanDefinition> getAllBeanDefinitions() {
        return new HashSet<>(beanDefinitionMap.values());
    }

    public void loadBeanDefinition(BeanDefinitionParser parser, File xml) throws Exception {
        if (xml.isFile() && xml.exists()) {
            Set<BeanDefinition> result = parser.parse(xml);
            result.stream().filter(definition -> isNotBlank(definition.getId())).
                    forEach(definition -> beanDefinitionMap.putIfAbsent(definition.getId(), definition));
        }
    }

}

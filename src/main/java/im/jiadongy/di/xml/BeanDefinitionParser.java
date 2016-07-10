package im.jiadongy.di.xml;

import im.jiadongy.di.core.definition.BeanDefinition;

import java.io.File;
import java.util.Set;

/**
 * Created by jiadongy on 16/7/10.
 */
public interface BeanDefinitionParser {

    Set<BeanDefinition> parse(File xmlFile) throws Exception;
}

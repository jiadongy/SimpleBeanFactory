package im.jiadongy.di.xml;

import com.google.common.base.Strings;
import im.jiadongy.di.core.beanfactory.BeanDefinitionManager;
import im.jiadongy.di.core.definition.BeanDefinition;
import im.jiadongy.di.core.definition.ConstructorArgs;
import im.jiadongy.di.core.definition.Property;
import im.jiadongy.di.core.exception.SimpleDIException;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import static im.jiadongy.di.core.definition.ConstructorArgs.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by jiadongy on 16/7/8.
 */
public class BeanDefinitionXMLParser implements BeanDefinitionParser {

    static DocumentBuilder documentBuilder;
    private BeanDefinitionManager beanDefinitionManager;

    public BeanDefinitionXMLParser(BeanDefinitionManager beanDefinitionManager) {
        this.beanDefinitionManager = beanDefinitionManager;
    }

    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        if (documentBuilder == null)
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return documentBuilder;
    }

    @Override
    public Set<BeanDefinition> parse(File xmlFile) throws IOException, SAXException, ParserConfigurationException, SimpleDIException {
        Set<BeanDefinition> result = parseRoot(xmlFile);
        return result;
    }

    private Set<BeanDefinition> parseRoot(File xmlFile) throws ParserConfigurationException, IOException, SAXException, SimpleDIException {
        Document document = getDocumentBuilder().parse(xmlFile);
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        Set<BeanDefinition> definitions = new LinkedHashSet<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.getTagName().equalsIgnoreCase("bean")) {
                    //loadBeanDefinition bean element
                    BeanDefinition definition = parseBeanNode(element);
                    if (definition != null)
                        definitions.add(definition);
                }
            }

        }
        return definitions;
    }

    private BeanDefinition parseBeanNode(Element element) throws SimpleDIException {
        if (StringUtils.isBlank(element.getAttribute("id")) && StringUtils.isBlank(element.getAttribute("class")))
            throw new SimpleDIException("parser : attr id or class missing");

        BeanDefinition definition;
        if (StringUtils.isBlank(element.getAttribute("id"))) {
            definition = beanDefinitionManager.getBeanDefinitionById(element.getAttribute("id"));
            if (definition != null)
                return definition;
        }

        definition = new BeanDefinition();

        definition.setId(element.getAttribute("id"));
        definition.setClazz(element.getAttribute("class"));

        definition.setInnerBean(Strings.isNullOrEmpty(definition.getId()) && !Strings.isNullOrEmpty(definition.getClazz()));

        definition.setInitMethod(element.getAttribute("init-method"));

        definition.setFactoryMethod(element.getAttribute("factory-method"));

        definition.setFactoryBean(element.getAttribute("factory-bean"));

        NodeList nodes = element.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equalsIgnoreCase("property")) {
                    Property property = parsePropertyNode((Element) node);
                    definition.getProperties().add(property);
                } else if (node.getNodeName().equalsIgnoreCase("constructor-arg")) {
                    ConstructorArgs constructorArgs = parseConstructorArgsNode((Element) node);
                    definition.getConstructorArgs().add(constructorArgs);
                }
            }
        }
        return definition;

    }

    private Property parsePropertyNode(Element element) throws SimpleDIException {

        Property info = new Property();

        if (StringUtils.isBlank(element.getAttribute("name")))
            throw new SimpleDIException("parser : attr name missing");

        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE && nodeList.item(i).getNodeName().equalsIgnoreCase("bean")) {
                Element e = (Element) nodeList.item(i);
                BeanDefinition definition = parseBeanNode(e);
                info.setType(Property.BEAN_DEFINITION);
                info.setDefinition(definition);
                return info;
            }
        }

        if (StringUtils.isBlank(element.getAttribute("value")) && StringUtils.isBlank(element.getAttribute("ref")))
            throw new SimpleDIException("parser : attr value or ref missing");
        else if (StringUtils.isNotBlank(element.getAttribute("value")) && StringUtils.isNotBlank(element.getAttribute("ref")))
            throw new SimpleDIException("parser : attr value or ref duplicate");

        info.setName(element.getAttribute("name"));

        info.setType(isNotBlank(element.getAttribute("ref")) ? Property.BEAN_REF : Property.VALUE);

        info.setRef(element.getAttribute("ref"));

        info.setValue(element.getAttribute("value"));

        return info;
    }

    private ConstructorArgs parseConstructorArgsNode(Element element) throws SimpleDIException {
        ConstructorArgs info = new ConstructorArgs();

        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE && nodeList.item(i).getNodeName().equalsIgnoreCase("bean")) {
                Element e = (Element) nodeList.item(i);
                BeanDefinition definition = parseBeanNode(e);
                info.setType(BEAN_DEFINITION);
                info.setDefinition(definition);
                return info;
            }
        }

        if (StringUtils.isBlank(element.getAttribute("value")) && StringUtils.isBlank(element.getAttribute("ref")))
            throw new SimpleDIException("parser : attr value or ref missing");
        else if (StringUtils.isBlank(element.getAttribute("value")) && StringUtils.isBlank(element.getAttribute("ref")))
            throw new SimpleDIException("parser : attr value or ref duplicate");

        else {


            info.setType(StringUtils.isNotBlank(element.getAttribute("ref")) ? BEAN_REF : VALUE);

            info.setRef(element.getAttribute("ref"));

            info.setValue(element.getAttribute("value"));

            return info;
        }

    }


}

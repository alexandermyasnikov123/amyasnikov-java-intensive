package net.dunice.intensive.spring_core.beans;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DefinitionExample {
    private final ConfigurableListableBeanFactory factory;

    @PostConstruct
    public void postConstruct() {
        /*printScannedBeans();
        printInConfigurationConfiguredBeans();
        printXmlConfiguredBeans();*/
    }

    public void printScannedBeans() {
        System.out.println("============Scanned beans in classpath====================");
        Arrays.stream(factory.getBeanDefinitionNames())
                .map(factory::getBeanDefinition)
                .filter(definition -> definition instanceof ScannedGenericBeanDefinition)
                .forEach(System.out::println);
        System.out.println("===========================================================");
    }

    public void printInConfigurationConfiguredBeans() {
        System.out.println("======In configuration configured beans in classpath=======");
        Arrays.stream(factory.getBeanDefinitionNames())
                .map(factory::getBeanDefinition)
                .filter(definition -> definition.getClass().getName().contains("ConfigurationClassBeanDefinition"))
                .forEach(System.out::println);
        System.out.println("============================================================");
    }

    public void printXmlConfiguredBeans() {
        System.out.println("===========XML Configured beans in classpath=================");
        Arrays.stream(factory.getBeanDefinitionNames())
                .map(factory::getBeanDefinition)
                .filter(definition -> definition.toString().trim().endsWith(".xml]"))
                .forEach(System.out::println);
        System.out.println("==============================================================");
    }
}

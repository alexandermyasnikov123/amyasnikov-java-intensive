package net.dunice.intensive.spring_core.beans;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostProcessorsConfiguration {

    @Bean(initMethod = "init")
    public MutableUserData mutableUserData() {
        final var data = new MutableUserData();
        System.out.println("Created object of " + data);
        return data;
    }

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> System.out.println("BeanFactoryPostProcessor.postProcessBeanFactory called");
    }
}

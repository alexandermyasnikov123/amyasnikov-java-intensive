package net.dunice.intensive.spring_core.beans;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Data
public class MutableUserData implements AutoCloseable, InitializingBean, DisposableBean, AgeOwner {
    private boolean isAdult;

    private String name;

    private int age;

    private String title;

    @Value("${mutable.user.data.details}")
    private String fieldInjectedDetails;

    @Override
    public void afterPropertiesSet() {
        System.out.println("After properties set: " + this);
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("PostConstruct called: " + this);
    }

    public void init() {
        age = 12;
        System.out.println("Init called: " + this);
    }

    @Override
    public void close() {
        System.out.println("Close called by Spring IoC Container");
    }

    @Override
    public void destroy() {
        System.out.println("Destroy called by Spring IoC Container");
    }

    @Autowired
    public void setTitle(@Value("${mutable.user.data.title}") String title) {
        System.out.println("Before setter called " + this);
        this.title = title;
        System.out.println("After setter called " + this);
    }
}

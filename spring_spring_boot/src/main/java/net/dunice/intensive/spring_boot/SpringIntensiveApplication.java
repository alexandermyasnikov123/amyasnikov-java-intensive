package net.dunice.intensive.spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringIntensiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntensiveApplication.class, args);
    }
}

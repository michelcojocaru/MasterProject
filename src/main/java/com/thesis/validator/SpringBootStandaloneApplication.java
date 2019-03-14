package com.thesis.validator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootStandaloneApplication implements CommandLineRunner {

    public static void main(String[] args) {
       SpringApplication.run(SpringBootStandaloneApplication.class, args);
    }
    @Bean
    public HelloService getHelloService(){
        return  new DefaultHelloService();
    }

    @Override
    public void run(String... args) throws Exception {
        getHelloService().hello();
    }
}

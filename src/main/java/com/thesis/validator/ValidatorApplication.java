package com.thesis.validator;

import com.thesis.validator.config.ClocInstaller;
import com.thesis.validator.file.ExternalProgramExecutor;
import com.thesis.validator.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class ValidatorApplication {


    public static void main(String[] args) {
        boolean clocInit = ClocInstaller.installCloc();
        if(clocInit){
            System.out.println("Successfully initialized cloc!");
        }
        SpringApplication.run(ValidatorApplication.class, args);
    }



}

package de.ines;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = { "de.ines.domain" })
@EnableJpaRepositories(basePackages = { "de.ines.repositories" })
@EnableRabbit
public class SmartCityPreprocessing {

    public static void main(String[] args){
        SpringApplication.run(SmartCityPreprocessing.class);
    }
}

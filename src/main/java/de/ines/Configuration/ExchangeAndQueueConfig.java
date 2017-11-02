package de.ines.Configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@org.springframework.context.annotation.Configuration
public class ExchangeAndQueueConfig {


    @Bean
    Queue preprocessingQueue(){
        return new Queue("preprocessingQueue", false);
    }

    @Bean
    FanoutExchange exchange(){
        return new FanoutExchange("SmartCity-Exchange1");
    }

    @Bean
    List<Binding> bindings(){
        return Arrays.asList(BindingBuilder.bind(preprocessingQueue()).to(exchange()));
    }
}

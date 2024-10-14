package io.github.clovisgargione.msleitor.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queues.excluir-estante}")
    private String excluirEstanteFila;
    
    @Bean
    public Queue queueExcluirEstante() {
	return new Queue(excluirEstanteFila, true);
    }
}

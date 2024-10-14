package io.github.clovisgargione.msautenticacao.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queues.excluir-leitor}")
    private String excluirLeitorFila;
    
    @Bean
    public Queue queueExcluirLeitor() {
	return new Queue(excluirLeitorFila, true);
    }
}

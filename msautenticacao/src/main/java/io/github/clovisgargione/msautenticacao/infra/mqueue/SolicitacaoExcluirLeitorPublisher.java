package io.github.clovisgargione.msautenticacao.infra.mqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.clovisgargione.msautenticacao.application.representation.DadosSolicitacaoExcluirLeitor;

@Component
public class SolicitacaoExcluirLeitorPublisher {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoExcluirLeitorPublisher.class);
    
    private RabbitTemplate rabbitTemplate;
    private Queue queueExcluirLeitor;
    
    public SolicitacaoExcluirLeitorPublisher(RabbitTemplate rabbitTemplate, Queue queueExcluirLeitor) {
	super();
	this.rabbitTemplate = rabbitTemplate;
	this.queueExcluirLeitor = queueExcluirLeitor;
    }
    
    public void solicitarExcluirLeitor(DadosSolicitacaoExcluirLeitor dadosSolicitacaoExcluirLeitor) throws JsonProcessingException {
	String json = convertIntoJson(dadosSolicitacaoExcluirLeitor);
	rabbitTemplate.convertAndSend(queueExcluirLeitor.getName(), json);
	logger.info("Solicitação para excluir leitor enviada..");
    }
    
    private String convertIntoJson(DadosSolicitacaoExcluirLeitor dadosSolicitacaoExcluirLeitor) throws JsonProcessingException{
	ObjectMapper mapper = new ObjectMapper();
	String json = mapper.writeValueAsString(dadosSolicitacaoExcluirLeitor);
	return json;
    }
}

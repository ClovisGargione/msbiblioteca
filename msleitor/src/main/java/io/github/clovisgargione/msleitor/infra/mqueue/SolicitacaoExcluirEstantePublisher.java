package io.github.clovisgargione.msleitor.infra.mqueue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.clovisgargione.msleitor.application.representation.DadosSolicitacaoExcluirEstante;

@Component
public class SolicitacaoExcluirEstantePublisher {

    private RabbitTemplate rabbitTemplate;
    private Queue queueExcluirEstante;

    public SolicitacaoExcluirEstantePublisher(RabbitTemplate rabbitTemplate, Queue queueExcluirEstante) {
	super();
	this.rabbitTemplate = rabbitTemplate;
	this.queueExcluirEstante = queueExcluirEstante;
    }

    public void solicitarExcluirEstante(DadosSolicitacaoExcluirEstante dadosSolicitacaoExcluirEstante)
	    throws JsonProcessingException {
	String json = convertIntoJson(dadosSolicitacaoExcluirEstante);
	rabbitTemplate.convertAndSend(queueExcluirEstante.getName(), json);
    }

    private String convertIntoJson(DadosSolicitacaoExcluirEstante dadosSolicitacaoExcluirEstante)
	    throws JsonProcessingException {
	ObjectMapper mapper = new ObjectMapper();
	String json = mapper.writeValueAsString(dadosSolicitacaoExcluirEstante);
	return json;
    }

}

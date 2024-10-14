package io.github.clovisgargione.msestante.infra.mqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.clovisgargione.msestante.application.EstanteService;
import io.github.clovisgargione.msestante.application.exception.EstanteException;
import io.github.clovisgargione.msestante.application.representation.DadosSolicitacaoExcluirEstante;

@Component
public class ExcluirEstanteSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(ExcluirEstanteSubscriber.class);
    
    private EstanteService estanteService;

    public ExcluirEstanteSubscriber(EstanteService estanteService) {
	super();
	this.estanteService = estanteService;
    }
    
    @RabbitListener(queues = "${mq.queues.excluir-estante}")
    public void receberSolicitacaoExcluirEstante(@Payload String payload) {
	try {
	    ObjectMapper mapper = new ObjectMapper();
	    DadosSolicitacaoExcluirEstante dados = mapper.readValue(payload, DadosSolicitacaoExcluirEstante.class);
	    estanteService.remover(dados.getIdLeitor());
	    logger.info("Solicitação para excluir estante recebida..");
	} catch(EstanteException e) {
	    logger.error("Erro ao receber solicitação para excluir estante: {}", e.getMessage());
	} catch (JsonProcessingException e) {
	    logger.error("Erro ao receber solicitação para excluir estante: {}", e.getMessage());
	}
    }
}

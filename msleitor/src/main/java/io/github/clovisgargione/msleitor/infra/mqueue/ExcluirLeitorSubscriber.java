package io.github.clovisgargione.msleitor.infra.mqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.clovisgargione.msleitor.application.LeitorService;
import io.github.clovisgargione.msleitor.application.exception.LeitorException;
import io.github.clovisgargione.msleitor.application.representation.DadosSolicitacaoExcluirEstante;
import io.github.clovisgargione.msleitor.application.representation.DadosSolicitacaoExcluirLeitor;
import io.github.clovisgargione.msleitor.domain.Leitor;


@Component
public class ExcluirLeitorSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(ExcluirLeitorSubscriber.class);
    
    private LeitorService service;
    
    private SolicitacaoExcluirEstantePublisher excluirEstantePublisher;

    public ExcluirLeitorSubscriber(LeitorService service, SolicitacaoExcluirEstantePublisher excluirEstantePublisher) {
	super();
	this.service = service;
	this.excluirEstantePublisher = excluirEstantePublisher;
    }
    
    @RabbitListener(queues = "${mq.queues.excluir-leitor}")
    public void receberSolicitacaoExcluirLeitor(@Payload String payload) {
	try {
	    ObjectMapper mapper = new ObjectMapper();
	    DadosSolicitacaoExcluirLeitor dados = mapper.readValue(payload, DadosSolicitacaoExcluirLeitor.class);
	    Leitor leitor = service.buscarPorIdUsuario(dados.getIdUsuario());
	    service.removerPorUsuario(dados.getIdUsuario());
	    logger.info("Solicitação para excluir leitor recebida..");
	    excluirEstantePublisher.solicitarExcluirEstante(new DadosSolicitacaoExcluirEstante(leitor.getId()));
	    logger.info("Solicitação para excluir estante enviada..");
	} catch(LeitorException e) {
	    logger.error("Erro ao receber solicitação para excluir leitor: {}", e.getMessage());
	} catch (JsonProcessingException e) {
	    logger.error("Erro ao receber solicitação para excluir leitor: {}", e.getMessage());
	}
    }
}

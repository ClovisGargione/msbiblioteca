package io.github.clovisgargione.msautenticacao.infra.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.clovisgargione.msautenticacao.application.representation.LeitorRequest;

@FeignClient(value = "msleitor", path = "/api/v1/secure/leitor")
public interface LeitorResourceClient {

    @PostMapping
    ResponseEntity<LeitorRequest> criarLeitor(@RequestBody LeitorRequest leitorRequest);
}

package io.github.clovisgargione.msestante.infra.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.clovisgargione.msestante.application.representation.LeitorResponse;

@FeignClient(value = "msleitor", path = "/api/v1/secure/leitor")
public interface LeitorResourceClient {

    @GetMapping(params = "id")
    ResponseEntity<LeitorResponse> buscarPorId(@RequestParam("id") Integer id);
}

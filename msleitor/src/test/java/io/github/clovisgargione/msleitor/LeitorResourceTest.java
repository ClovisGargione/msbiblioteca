package io.github.clovisgargione.msleitor;

import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import io.github.clovisgargione.msleitor.application.LeitorService;
import io.github.clovisgargione.msleitor.application.exception.LeitorException;
import io.github.clovisgargione.msleitor.domain.Leitor;

@SpringBootTest
@AutoConfigureMockMvc
public class LeitorResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeitorService service;

    @Test
    void criar_leitor_test() throws Exception {

	Mockito.when(service.criar(Mockito.any())).thenReturn(new Leitor(1, "Clovis Gargione", 1));
	ResultActions result = mockMvc.perform(post("/secure/leitor").contentType(MediaType.APPLICATION_JSON)
		.content("{\"id\": 1, \"nome\": \"Clovis Gargione\", \"idUsuario\": 1}"));

	result.andExpect(status().isCreated()).andExpect(jsonPath("$").exists()).andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nome").value("Clovis Gargione")).andExpect(jsonPath("$.idUsuario").value(1));
    }

    @Test
    void atualizar_leitor_test() throws Exception {

	Mockito.when(service.atualizar(Mockito.any())).thenReturn(new Leitor(1, "Clovis Gargione Rodrigues", 1));
	ResultActions result = mockMvc.perform(put("/secure/leitor").contentType(MediaType.APPLICATION_JSON)
		.content("{\"id\": 1, \"nome\": \"Clovis Gargione Rodrigues\", \"idUsuario\": 1}"));

	result.andExpect(status().isOk()).andExpect(jsonPath("$").exists()).andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nome").value("Clovis Gargione Rodrigues"))
		.andExpect(jsonPath("$.idUsuario").value(1));
    }

    @Test
    void buscar_leitor_por_id_test() throws Exception {

	Mockito.when(service.buscarPorId(Mockito.any())).thenReturn(new Leitor(1, "Clovis Gargione Rodrigues", 1));
	ResultActions result = mockMvc
		.perform(get("/secure/leitor").contentType(MediaType.APPLICATION_JSON).param("id", "1"));

	result.andExpect(status().isOk()).andExpect(jsonPath("$").exists()).andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nome").value("Clovis Gargione Rodrigues"))
		.andExpect(jsonPath("$.idUsuario").value(1));
    }

    @Test
    void buscar_leitor_por_id_usuario_test() throws Exception {

	Mockito.when(service.buscarPorIdUsuario(Mockito.any()))
		.thenReturn(new Leitor(1, "Clovis Gargione Rodrigues", 1));
	ResultActions result = mockMvc
		.perform(get("/secure/leitor/usuario").contentType(MediaType.APPLICATION_JSON).param("idUsuario", "1"));

	result.andExpect(status().isOk()).andExpect(jsonPath("$").exists()).andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.nome").value("Clovis Gargione Rodrigues"))
		.andExpect(jsonPath("$.idUsuario").value(1));
    }

    @Test
    void remover_leitor_por_id_test() throws Exception {

	ResultActions result = mockMvc
		.perform(delete("/secure/leitor").contentType(MediaType.APPLICATION_JSON).param("id", "1"));

	result.andExpect(status().isNoContent());
    }

    @Test
    void remover_leitor_por_id_usuario_test() throws Exception {

	ResultActions result = mockMvc.perform(
		delete("/secure/leitor/usuario").contentType(MediaType.APPLICATION_JSON).param("idUsuario", "1"));

	result.andExpect(status().isNoContent());
    }

    @Test
    void server_test() throws Exception {

	ResultActions result = mockMvc.perform(get("/secure/leitor/server").contentType(MediaType.APPLICATION_JSON));

	result.andExpect(content().string(anything()));
    }

    @Test
    void criar_leitor_exception_test() throws Exception {

	Mockito.when(service.criar(Mockito.any()))
		.thenThrow(new LeitorException("O leitor % já foi cadastrado.".replace("%", "Clovis Gargione")));
	ResultActions result = mockMvc.perform(post("/secure/leitor").contentType(MediaType.APPLICATION_JSON)
		.content("{\"id\": 1, \"nome\": \"Clovis Gargione\", \"idUsuario\": 1}"));

	result.andExpect(status().isConflict()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
		.andExpect(content().string("O leitor Clovis Gargione já foi cadastrado."));
    }

    @Test
    void atualizar_leitor_exception_test() throws Exception {

	Mockito.when(service.atualizar(Mockito.any())).thenThrow(new LeitorException("Leitor não econtrado."));
	ResultActions result = mockMvc.perform(put("/secure/leitor").contentType(MediaType.APPLICATION_JSON)
		.content("{\"id\": 1, \"nome\": \"Clovis Gargione\", \"idUsuario\": 1}"));

	result.andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
		.andExpect(content().string("Leitor não econtrado."));
    }

    @Test
    void buscar_leitor_por_id_exception_test() throws Exception {

	Mockito.when(service.buscarPorId(Mockito.any())).thenThrow(new LeitorException("Leitor não econtrado."));
	ResultActions result = mockMvc
		.perform(get("/secure/leitor").contentType(MediaType.APPLICATION_JSON).param("id", "1"));

	result.andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
		.andExpect(content().string("Leitor não econtrado."));
    }

    @Test
    void buscar_leitor_por_id_usuario_exception_test() throws Exception {

	Mockito.when(service.buscarPorIdUsuario(Mockito.any())).thenThrow(new LeitorException("Leitor não econtrado."));
	ResultActions result = mockMvc
		.perform(get("/secure/leitor/usuario").contentType(MediaType.APPLICATION_JSON).param("idUsuario", "1"));

	result.andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
		.andExpect(content().string("Leitor não econtrado."));
    }
    
    @Test
    void remover_leitor_por_id_exception_test() throws Exception {
	doThrow(new LeitorException("Leitor não econtrado.")).when(service).remover(Mockito.any());
	ResultActions result = mockMvc
		.perform(delete("/secure/leitor").contentType(MediaType.APPLICATION_JSON).param("id", "1"));

	result.andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
	.andExpect(content().string("Leitor não econtrado."));
    }

    @Test
    void remover_leitor_por_id_usuario_exception_test() throws Exception {
	doThrow(new LeitorException("Leitor não econtrado.")).when(service).removerPorUsuario(Mockito.any());
	ResultActions result = mockMvc.perform(
		delete("/secure/leitor/usuario").contentType(MediaType.APPLICATION_JSON).param("idUsuario", "1"));

	result.andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
	.andExpect(content().string("Leitor não econtrado."));
    }
}

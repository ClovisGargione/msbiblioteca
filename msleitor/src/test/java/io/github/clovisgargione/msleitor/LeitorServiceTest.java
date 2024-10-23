package io.github.clovisgargione.msleitor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.clovisgargione.msleitor.application.LeitorService;
import io.github.clovisgargione.msleitor.application.exception.LeitorException;
import io.github.clovisgargione.msleitor.application.representation.LeitorRequest;
import io.github.clovisgargione.msleitor.domain.Leitor;
import io.github.clovisgargione.msleitor.infra.repository.LeitorRepository;

@SpringBootTest
public class LeitorServiceTest {

    @Mock
    private LeitorRepository repository;

    @InjectMocks
    private LeitorService service;

    @Test
    void criar_test() throws LeitorException {
	Mockito.when(repository.findByIdUsuario(Mockito.any())).thenReturn(Optional.empty());
	Mockito.when(repository.save(Mockito.any())).thenReturn(new Leitor(1, "Clovis", 1));

	Leitor leitorReturn = service.criar(new LeitorRequest(1, "Clovis", 1));
	assertThat(leitorReturn).isNotNull();
	assertThat(leitorReturn.getId()).isEqualTo(1);
	assertThat(leitorReturn.getNome()).isEqualTo("Clovis");
	assertThat(leitorReturn.getIdUsuario()).isEqualTo(1);
    }

    @Test
    void criar_leitor_exception_test() throws LeitorException {
	Mockito.when(repository.findByIdUsuario(Mockito.any()))
		.thenReturn(Optional.ofNullable(new Leitor(1, "Clovis", 1)));

	assertThatThrownBy(() -> service.criar(new LeitorRequest(1, "Clovis", 1))).isInstanceOf(LeitorException.class)
		.hasMessageContaining("O leitor Clovis já foi cadastrado.");
    }

    @Test
    void atualizar_test() throws LeitorException {
	Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.ofNullable(new Leitor(1, "Clovis", 1)));
	Mockito.when(repository.save(Mockito.any())).thenReturn(new Leitor(1, "Clovis Gargione", 1));

	Leitor leitorReturn = service.atualizar(new LeitorRequest(1, "Clovis Gargione", 1));
	assertThat(leitorReturn).isNotNull();
	assertThat(leitorReturn.getId()).isEqualTo(1);
	assertThat(leitorReturn.getNome()).isEqualTo("Clovis Gargione");
	assertThat(leitorReturn.getIdUsuario()).isEqualTo(1);
    }

    @Test
    void atualizar_leitor_exception_test() throws LeitorException {
	Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());

	assertThatThrownBy(() -> service.atualizar(new LeitorRequest(1, "Clovis", 1)))
		.isInstanceOf(LeitorException.class).hasMessageContaining("Leitor não econtrado");
    }

    @Test
    void buscar_por_id_test() throws LeitorException {
	Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.ofNullable(new Leitor(1, "Clovis", 1)));

	Leitor leitorReturn = service.buscarPorId(1);
	assertThat(leitorReturn).isNotNull();
	assertThat(leitorReturn.getId()).isEqualTo(1);
	assertThat(leitorReturn.getNome()).isEqualTo("Clovis");
	assertThat(leitorReturn.getIdUsuario()).isEqualTo(1);
    }

    @Test
    void buscar_por_id_exception_test() throws LeitorException {
	Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());
	assertThatThrownBy(() -> service.buscarPorId(1)).isInstanceOf(LeitorException.class)
		.hasMessageContaining("Leitor não econtrado");
    }

    @Test
    void buscar_por_id_usuario_test() throws LeitorException {
	Mockito.when(repository.findByIdUsuario(Mockito.any()))
		.thenReturn(Optional.ofNullable(new Leitor(1, "Clovis", 1)));

	Leitor leitorReturn = service.buscarPorIdUsuario(1);
	assertThat(leitorReturn).isNotNull();
	assertThat(leitorReturn.getId()).isEqualTo(1);
	assertThat(leitorReturn.getNome()).isEqualTo("Clovis");
	assertThat(leitorReturn.getIdUsuario()).isEqualTo(1);
    }

    @Test
    void buscar_por_id_usuario_exception_test() throws LeitorException {
	Mockito.when(repository.findByIdUsuario(Mockito.any())).thenReturn(Optional.empty());
	assertThatThrownBy(() -> service.buscarPorIdUsuario(1)).isInstanceOf(LeitorException.class)
		.hasMessageContaining("Leitor não econtrado");
    }

    @Test
    void remover_test() throws LeitorException {
	Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.ofNullable(new Leitor(1, "Clovis", 1)));
	doNothing().when(repository).delete(Mockito.any());
	service.remover(1);
	verify(repository, times(1)).delete(Mockito.any());
    }

    @Test
    void remover_exception_test() throws LeitorException {
	Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());
	assertThatThrownBy(() -> service.remover(1)).isInstanceOf(LeitorException.class)
		.hasMessageContaining("Leitor não econtrado");
    }

    @Test
    void remover_por_usuario_test() throws LeitorException {
	Mockito.when(repository.findByIdUsuario(Mockito.any()))
		.thenReturn(Optional.ofNullable(new Leitor(1, "Clovis", 1)));
	doNothing().when(repository).delete(Mockito.any());
	service.removerPorUsuario(1);
	verify(repository, times(1)).delete(Mockito.any());
    }

    @Test
    void remover_por_usuario_exception_test() throws LeitorException {
	Mockito.when(repository.findByIdUsuario(Mockito.any())).thenReturn(Optional.empty());
	assertThatThrownBy(() -> service.removerPorUsuario(1)).isInstanceOf(LeitorException.class)
		.hasMessageContaining("Leitor não econtrado");
    }

}

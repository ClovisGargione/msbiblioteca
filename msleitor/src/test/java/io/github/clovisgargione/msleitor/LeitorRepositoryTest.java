package io.github.clovisgargione.msleitor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.github.clovisgargione.msleitor.domain.Leitor;
import io.github.clovisgargione.msleitor.infra.repository.LeitorRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LeitorRepositoryTest {

    @Autowired
    private LeitorRepository repository;

    @Test
    public void find_all_test() {

	List<Leitor> leitor = repository.findAll();
	assertThat(leitor).isNotEmpty();
    }

    @Test
    public void find_by_id_test() {

	Optional<Leitor> leitor = Optional.ofNullable(repository.findById(4).orElse(null));
	assertThat(leitor.get()).isNotNull();
    }

    @Test
    public void find_by_id_null_test() {

	Optional<Leitor> leitor = Optional.ofNullable(repository.findById(0).orElse(null));
	assertThat(leitor).isEmpty();
    }

    @Test
    public void count_test() {

	long count = repository.count();

	assertThat(1).isEqualTo(count);
    }

    @Test
    public void exists_test() {

	boolean exists = repository.existsById(4);

	assertThat(exists).isTrue();
    }

    @Test
    public void not_exists_test() {

	boolean exists = repository.existsById(1);

	assertThat(exists).isFalse();
    }

}

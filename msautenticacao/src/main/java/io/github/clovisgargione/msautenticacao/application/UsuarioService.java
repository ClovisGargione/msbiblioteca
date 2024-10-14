package io.github.clovisgargione.msautenticacao.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import feign.FeignException.FeignClientException;
import io.github.clovisgargione.msautenticacao.application.exception.ErroComunicacaoMicroservicesException;
import io.github.clovisgargione.msautenticacao.application.exception.UsuarioException;
import io.github.clovisgargione.msautenticacao.application.representation.AuthorityDTO;
import io.github.clovisgargione.msautenticacao.application.representation.DadosSolicitacaoExcluirLeitor;
import io.github.clovisgargione.msautenticacao.application.representation.LeitorRequest;
import io.github.clovisgargione.msautenticacao.application.representation.UsuarioRequest;
import io.github.clovisgargione.msautenticacao.domain.usuario.Authority;
import io.github.clovisgargione.msautenticacao.domain.usuario.Credenciais;
import io.github.clovisgargione.msautenticacao.domain.usuario.Usuario;
import io.github.clovisgargione.msautenticacao.infra.clients.LeitorResourceClient;
import io.github.clovisgargione.msautenticacao.infra.mqueue.SolicitacaoExcluirLeitorPublisher;
import io.github.clovisgargione.msautenticacao.infra.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private UsuarioRepository repository;

    private PasswordEncoder userPasswordEncoder;

    private LeitorResourceClient leitorResourceClient;
    
    private SolicitacaoExcluirLeitorPublisher excluirLeitorPublisher;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder userPasswordEncoder,
	    LeitorResourceClient leitorResourceClient, SolicitacaoExcluirLeitorPublisher excluirLeitorPublisher) {
	super();
	this.repository = repository;
	this.userPasswordEncoder = userPasswordEncoder;
	this.leitorResourceClient = leitorResourceClient;
	this.excluirLeitorPublisher = excluirLeitorPublisher;
    }
    
    public Usuario cadastrar(UsuarioRequest usuarioRequest) throws UsuarioException, ErroComunicacaoMicroservicesException {
	String usuario = usuarioRequest.getCredenciais().getUsuario();
	if (repository.findByUsuario(usuario).isPresent()) {
	    throw new UsuarioException("Usuário já cadastrado!");
	}
	String senha = usuarioRequest.getCredenciais().getSenha();
	Credenciais credenciais = new Credenciais(usuario, userPasswordEncoder.encode(senha),
		usuarioRequest.getCredenciais().isHabilitado(),
		usuarioRequest.getCredenciais().getDataUltimaRedefinicaoDeSenha(),
		usuarioRequest.getCredenciais().isContaNaoExpirada(), usuarioRequest.getCredenciais().isContaNaoBloqueada(),
		usuarioRequest.getCredenciais().isCredencialNaoExpirada());
	List<Authority> authorities = new ArrayList<>();
	authorities.addAll(mapToAutorities(usuarioRequest.getAuthorities()));
	String nome = usuarioRequest.getNome();
	Usuario user = new Usuario(nome, credenciais, authorities);
	repository.save(user);
	try {
	    leitorResourceClient.criarLeitor(new LeitorRequest(user.getNome(), user.getId().intValue()));
	} catch(FeignClientException fce) {
	    throw new ErroComunicacaoMicroservicesException(fce.getMessage(), fce.status());
	}
	return user;
    }
    
    public Usuario buscarPorLogin(String usuario) throws UsuarioException {
	Optional<Usuario> usuarioOpt = repository.findByUsuario(usuario);
	if (usuarioOpt.isEmpty()) {
	    throw new UsuarioException("Usuário login % não encontrado!".replace("%", usuario));
	}
	return usuarioOpt.get();
    }
    
    public void removerUsuario(Long id) throws UsuarioException {
	Optional<Usuario> usuario = repository.findById(id);
	if(usuario.isEmpty()) {
	    throw new UsuarioException("Usuário id % não encontrado!".replace("%", id.toString()));
	}
	try {
	    excluirLeitorPublisher.solicitarExcluirLeitor(new DadosSolicitacaoExcluirLeitor(id.intValue()));
	} catch (JsonProcessingException e) {
	    throw new UsuarioException("Não foi possível construir os dados para a solicitação.");
	}
	repository.deleteById(id);
    }
    
    private Collection<? extends Authority> mapToAutorities(List<AuthorityDTO> authorities) {
	return authorities.stream().map(authority -> new Authority(authority.getName())).collect(Collectors.toList());
    }
}

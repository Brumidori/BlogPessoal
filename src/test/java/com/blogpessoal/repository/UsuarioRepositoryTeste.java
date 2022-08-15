package com.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.blogpessoal.model.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTeste {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
		usuarioRepository.save(new Usuario(0L,"teste","teste@email.com","senha123","url"));
		usuarioRepository.save(new Usuario(0L,"pedro silva","pedro@email2.com","senha123","url"));
		usuarioRepository.save(new Usuario(0L,"caio silva","silva@email3.com","senha123","url"));
		usuarioRepository.save(new Usuario(0L,"joao silva","joaosilva@email4.com","senha123","url"));
	}
	
	@Test
	@DisplayName("Retorna 1 Usuario")
	public void deveRetornarUmUsuario() {
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("teste@email.com");
		assertTrue(usuario.get().getUsuario().equals("teste@email.com"));
		
	}
	
	@Test
	@DisplayName("Retorna 3 Usuarios")
	public void deveRetornarTresUsuarios() {
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("silva");
		assertEquals(3, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("pedro silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("caio silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("joao silva"));
		
	}
	
	
}

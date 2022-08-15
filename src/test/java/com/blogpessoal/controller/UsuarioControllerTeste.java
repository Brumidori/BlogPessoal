package com.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blogpessoal.model.Usuario;
import com.blogpessoal.repository.UsuarioRepository;
import com.blogpessoal.service.UsuarioService;;

/*
 *  a anotação @SpringBootTest indica que a Classe UsuarioControllerTest é uma Classe Spring Boot Testing. 
 *  A Opção environment indica que caso a porta principal (8080 para uso local) esteja ocupada, o Spring irá atribuir uma outra porta automaticamente.
	a anotação @TestInstance indica que o Ciclo de vida da Classe de Teste será por Classe.
	 a anotação @TestMethodOrder indica em qual ordem os testes serão executados. 
	 A opção MethodOrderer.OrderAnnotation.class indica que os testes serão executados na ordem indicada pela anotação @Order inserida em cada teste.
	  Exemplo: @Order(1) 🡪 indica que este será o primeiro teste que será executado
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTeste {

		@Autowired
		private TestRestTemplate testRestTemplate;
		
		@Autowired
		private UsuarioService usuarioService;
		
		@Autowired
		private UsuarioRepository usuarioRepository;
		
		@BeforeAll
		void start() {
			
			usuarioRepository.deleteAll();
		}
		
		@Test
		@Order(1)
		@DisplayName("Cadastrar Um Usuário")
		public void deveCriarUmUsuario() {
			HttpEntity<Usuario>requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
					"Paulo Antunes", "paulo_antunes@email.com", "linkfoto",  "senhasenha"));
			
			ResponseEntity <Usuario> resposta = testRestTemplate
					.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
			
			assertEquals(HttpStatus.CREATED,resposta.getStatusCode());
			assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
			assertEquals(requisicao.getBody().getFoto(), resposta.getBody().getFoto());
			assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
		}
		
		@Test
		@Order(2)
		@DisplayName("Não deve permitir duplicação do usuário")
		public void naoDeveDuplicarUsuario() {
			usuarioService.cadastrarUsuario(new Usuario(0L, 
					"Maria Silva", "maria_silva@email.com", "linkfoto",  "senhasenha"));
			
			HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
					"Maria Silva", "maria_silva@email.com", "linkfoto",  "senhasenha"));
			
			ResponseEntity <Usuario> resposta = testRestTemplate
					.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
			
			assertEquals(HttpStatus.BAD_REQUEST,resposta.getStatusCode());
	
		}
		@Test
		@Order(3)
		@DisplayName("Atualizar um Usuário")
		public void deveAtualizarUmUsuario() {

			/**
			 * Persiste um objeto da Classe Usuario no Banco de dados através do Objeto da Classe UsuarioService e
			 * guarda o objeto persistido no Banco de Dadoas no Objeto usuarioCadastrado, que será reutilizado abaixo. 
			 * 
			 * O Objeto usuarioCadastrado será do tipo Optional porquê caso o usuário não seja persistido no Banco 
			 * de dados, o Optional evitará o erro NullPointerException (Objeto Nulo).
			 */
			Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "https://i.imgur.com/yDRVeK7.jpg"));
			/**
			 *  Cria um Objeto da Classe Usuário contendo os dados do Objeto usuarioCadastrado, que foi persistido na
			 *  linha anterior, alterando os Atributos Nome e Usuário (Atualização dos Atributos). 
			 *  
			 * Observe que para obter o Id de forma automática, foi utilizado o método getId() do Objeto usuarioCadastrado.
			 */
			Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), 
				"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123" , "https://i.imgur.com/yDRVeK7.jpg");
			
			/**
			 * Insere o objeto da Classe Usuario (usuarioUpdate) dentro de um Objeto da Classe HttpEntity (Entidade HTTP)
			 */
			HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);

			/**
			 * Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a Resposta da Requisição que será 
			 * enviada pelo Objeto da Classe TestRestTemplate.
			 * 
			 * Na requisição HTTP será enviada a URL do recurso (/usuarios/atualizar), o verbo (PUT), a entidade
			 * HTTP criada acima (corpoRequisicao) e a Classe de retornos da Resposta (Usuario).
			 * 
			 * Observe que o Método Atualizar não está liberado de autenticação (Login do usuário), por isso utilizamos o
			 * Método withBasicAuth para autenticar o usuário em memória, criado na BasicSecurityConfig.
			 * 
			 * Usuário: root
			 * Senha: root
			 */
			ResponseEntity<Usuario> corpoResposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

			/**
			 *  Verifica se a requisição retornou o Status Code OK (200) 
			 * Se for verdadeira, o teste passa, se não, o teste falha.
			 */
			assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());

			/**
			 * Verifica se o Atributo Nome do Objeto da Classe Usuario retornado no Corpo da Requisição 
			 * é igual ao Atributo Nome do Objeto da Classe Usuario Retornado no Corpo da Resposta
			 * Se for verdadeiro, o teste passa, senão o teste falha.
			 */
			assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());

			/**
			 * Verifica se o Atributo Usuario do Objeto da Classe Usuario retornado no Corpo da Requisição 
			 * é igual ao Atributo Usuario do Objeto da Classe Usuario Retornado no Corpo da Resposta
			 * Se for verdadeiro, o teste passa, senão o teste falha.
			 */
			assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
		}

		@Test
		@Order(4)
		@DisplayName("Listar todos os Usuários")
		public void deveMostrarTodosUsuarios() {

			/**
			 * Persiste dois objetos diferentes da Classe Usuario no Banco de dados através do Objeto da Classe UsuarioService
			 */
			usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));
			
			usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));

			/**
			 * Cria um Objeto da Classe ResponseEntity (corpoResposta), que receberá a Resposta da Requisição que será 
			 * enviada pelo Objeto da Classe TestRestTemplate.
			 * 
			 * Na requisição HTTP será enviada a URL do recurso (/usuarios/all), o verbo (GET), a entidade
			 * HTTP será nula (Requisição GET não envia nada no Corpo da Requisição) e a Classe de retorno da Resposta 
			 * (String), porquê a lista de Usuários será do tipo String.
			 * 
			 * Observe que o Método All não está liberado de autenticação (Login do usuário), por isso utilizamos o
			 * Método withBasicAuth para autenticar o usuário em memória, criado na BasicSecurityConfig.
			 * 
			 * Usuário: root
			 * Senha: root
			 */
			ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);

			/**
			 *  Verifica se a requisição retornou o Status Code OK (200) 
			 * Se for verdadeira, o teste passa, se não, o teste falha.
			 */
			assertEquals(HttpStatus.OK, resposta.getStatusCode());

		}
}

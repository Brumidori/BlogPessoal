package com.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogpessoal.model.Temas;
import com.blogpessoal.repository.TemasRepository;

@RestController
@RequestMapping ("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemasController {

	
	@Autowired
	private TemasRepository temasRepository;
	
	@GetMapping
	public ResponseEntity<List<Temas>> getAll(){
		return ResponseEntity.ok(temasRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Temas> getById(@PathVariable Long id){
		return temasRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		
	}
	
	@GetMapping ("/descricao/{descricao}")
	public ResponseEntity<List<Temas>> getByTitulo (@PathVariable String descricao){
		return ResponseEntity.ok(temasRepository.findAllByDescricaoContainingIgnoreCase(descricao));
	}
	
	@PostMapping
	public ResponseEntity<Temas> post (@RequestBody Temas temas){
		return ResponseEntity.status(HttpStatus.CREATED).body(temasRepository.save(temas));
	}
	
	@PutMapping
	public ResponseEntity<Temas> put (@RequestBody Temas temas){
		return ResponseEntity.status(HttpStatus.OK).body(temasRepository.save(temas));
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		temasRepository.deleteById(id);
	}
}

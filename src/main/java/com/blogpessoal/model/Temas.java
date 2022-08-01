package com.blogpessoal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table (name= "tb_temas")
public class Temas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id_temas;
	
	@NotBlank(message = "O atributo descricao é Obrigatório!")
	@Size(min = 10, max = 45, message = "O atributo descrição deve conter no mínimo 10 e no máximo 45 caracteres")
	private String descricao;

	public Long getId_temas() {
		return id_temas;
	}

	public void setId_temas(Long id_temas) {
		this.id_temas = id_temas;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}

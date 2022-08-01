package com.blogpessoal.repository;

import org.springframework.stereotype.Repository;

import com.blogpessoal.model.Temas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface TemasRepository extends JpaRepository <Temas, Long>{
	public List<Temas> findAllByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);
}

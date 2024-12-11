package br.usp.projetoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.usp.projetoa.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}

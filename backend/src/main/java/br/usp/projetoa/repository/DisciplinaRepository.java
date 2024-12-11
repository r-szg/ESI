package br.usp.projetoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.usp.projetoa.model.Disciplina;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, String> {
}

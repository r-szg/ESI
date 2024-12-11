package br.usp.projetoa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.usp.projetoa.model.Matricula;
import br.usp.projetoa.model.MatriculaId;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, MatriculaId> {
    List<Matricula> findByAluno_NumeroUsp(Long numeroUsp);
}

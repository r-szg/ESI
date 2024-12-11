package br.usp.projetoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.usp.projetoa.model.Docente;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
}

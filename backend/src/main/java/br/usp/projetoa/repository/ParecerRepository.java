package br.usp.projetoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.usp.projetoa.model.Parecer;

@Repository
public interface ParecerRepository extends JpaRepository<Parecer, Long> {
}

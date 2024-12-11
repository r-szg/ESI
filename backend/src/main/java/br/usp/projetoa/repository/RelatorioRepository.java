package br.usp.projetoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.usp.projetoa.model.Relatorio;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {
}

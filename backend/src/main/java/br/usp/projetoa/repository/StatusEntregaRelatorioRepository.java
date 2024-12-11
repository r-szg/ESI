package br.usp.projetoa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.usp.projetoa.enums.SituacaoEntregaRelatorio;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.StatusEntregaRelatorio;

@Repository
public interface StatusEntregaRelatorioRepository extends JpaRepository<StatusEntregaRelatorio, Long> {
    List<StatusEntregaRelatorio> findBySemestreAnoAndSituacaoEntregaRelatorio(String semestreAno, SituacaoEntregaRelatorio SituacaoEntregaRelatorio);
    StatusEntregaRelatorio findByAlunoAndSemestreAno(Aluno aluno, String semestreAno);
}

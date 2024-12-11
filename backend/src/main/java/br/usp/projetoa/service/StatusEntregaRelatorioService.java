package br.usp.projetoa.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.usp.projetoa.enums.SituacaoEntregaRelatorio;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.StatusEntregaRelatorio;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.StatusEntregaRelatorioRepository;
import br.usp.projetoa.util.SemestreUtil;

@Service
public class StatusEntregaRelatorioService {
    private final StatusEntregaRelatorioRepository statusEntregaRelatorioRepository;
    private final AlunoRepository alunoRepository;

    public StatusEntregaRelatorioService(StatusEntregaRelatorioRepository statusEntregaRelatorioRepository, AlunoRepository alunoRepository) {
        this.statusEntregaRelatorioRepository = statusEntregaRelatorioRepository;
        this.alunoRepository = alunoRepository;
    }

    @Scheduled(cron = "0 0 0 1 2,8 *")
    public void gerarStatusEntregaRelatorioParaNovoSemestre() {
        String semestreAnoAtual = SemestreUtil.determinaSemestreAnoAtual();

        List<Aluno> alunos = alunoRepository.findAll();

        for (Aluno aluno : alunos) {
            StatusEntregaRelatorio statusEntregaRelatorio = new StatusEntregaRelatorio(semestreAnoAtual, SituacaoEntregaRelatorio.PENDENTE);
            statusEntregaRelatorio.setAluno(aluno);

            statusEntregaRelatorioRepository.save(statusEntregaRelatorio);
        }
    }

    public List<StatusEntregaRelatorio> buscarTodosStatus() {
        return statusEntregaRelatorioRepository.findAll();
    }

    public StatusEntregaRelatorio buscarStatusPorAlunoNoSemestreAtual(Long numeroUspAluno) {
        List<StatusEntregaRelatorio> statusEntregaRelatorios = statusEntregaRelatorioRepository.findAll();

        String semestreAnoAtual = SemestreUtil.determinaSemestreAnoAtual();

        for (StatusEntregaRelatorio statusEntregaRelatorio : statusEntregaRelatorios) {
            if (statusEntregaRelatorio.getAluno().getNumeroUsp().equals(numeroUspAluno)
                && statusEntregaRelatorio.getSemestreAno().equals(semestreAnoAtual)) {

                return statusEntregaRelatorio;
            }
        }

        return null;
    }
}

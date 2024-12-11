package br.usp.projetoa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import br.usp.projetoa.dto.RelatorioDto;
import br.usp.projetoa.enums.SituacaoEntregaRelatorio;
import br.usp.projetoa.events.RelatorioCriadoEvent;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.Relatorio;
import br.usp.projetoa.model.StatusEntregaRelatorio;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DocenteRepository;
import br.usp.projetoa.repository.RelatorioRepository;
import br.usp.projetoa.repository.StatusEntregaRelatorioRepository;
import br.usp.projetoa.security.AuthenticatedUserUtil;
import br.usp.projetoa.util.SemestreUtil;

@Service
public class RelatorioService {
    private final RelatorioRepository relatorioRepository;
    private final AlunoRepository alunoRepository;
    private final DocenteRepository docenteRepository;
    private final StatusEntregaRelatorioRepository statusEntregaRelatorioRepository;

    private final ApplicationEventPublisher eventPublisher;

    public RelatorioService(RelatorioRepository relatorioRepository, AlunoRepository alunoRepository, 
        DocenteRepository docenteRepository, StatusEntregaRelatorioRepository statusEntregaRelatorioRepository, 
        ApplicationEventPublisher eventPublisher) {
        this.relatorioRepository = relatorioRepository;
        this.alunoRepository = alunoRepository;
        this.docenteRepository = docenteRepository;
        this.statusEntregaRelatorioRepository = statusEntregaRelatorioRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Relatorio> buscarTodosRelatorios() {
        return relatorioRepository.findAll();
    }

    public List<Relatorio> buscarRelatoriosPorAluno(Long numeroUsp) {
        List<Relatorio> todosRelatorios = relatorioRepository.findAll();
        List<Relatorio> relatoriosAluno = new ArrayList<>();

        for (Relatorio relatorio : todosRelatorios) {
            if (relatorio.getAluno().getNumeroUsp().equals(numeroUsp)) relatoriosAluno.add(relatorio);
        }

        return relatoriosAluno;
    }

    public List<Relatorio> buscarRelatoriosPorOrientador(Long numeroUsp) {
        List<Relatorio> todosRelatorios = relatorioRepository.findAll();
        List<Relatorio> relatoriosOrientador = new ArrayList<>();

        for (Relatorio relatorio : todosRelatorios) {
            if (relatorio.getDocente().getNumeroUsp().equals(numeroUsp)
                && relatorio.getDocente().getEhOrientador()) relatoriosOrientador.add(relatorio);
        }

        return relatoriosOrientador;
    }

    public Long criarRelatorio(RelatorioDto relatorioDto) {
        Long numeroUspLogado = AuthenticatedUserUtil.getNumeroUspLogado();

        if (!alunoRepository.existsById(numeroUspLogado)) throw new RuntimeException("Aluno com número USP " + numeroUspLogado + " não encontrado.");
        if (!docenteRepository.existsById(relatorioDto.numeroUspParecista())) throw new RuntimeException("Parecista com número USP " + relatorioDto.numeroUspParecista() + " não encontrado.");

        Relatorio relatorio = new Relatorio(
            relatorioDto.dataUltimaAtualizacaoLattes(), 
            relatorioDto.tipoCurso(), 
            relatorioDto.mesAnoIngressoComoAlunoRegular(), 
            relatorioDto.resultadoUltimoRelatorio(),
            relatorioDto.quantidadeAprovacoesDesdeInicioCurso(), 
            relatorioDto.quantidadeReprovacoes2023S2(), 
            relatorioDto.quantidadeReprovacoesDesdeInicioCurso(), 
            relatorioDto.realizouExameProficienciaLinguas(), 
            relatorioDto.realizouExameQualificacao(), 
            relatorioDto.prazoMaximoInscricaoExameQualificacao(), 
            relatorioDto.prazoMaximoDepositoDissertacao(), 
            relatorioDto.artigosEmFaseEscrita(), 
            relatorioDto.artigosSubmetidosEEmPeriodoAvaliacao(), 
            relatorioDto.artigosAceitosOuPublicados(), 
            relatorioDto.participacaoAtividadesAcademicas2024S1(), 
            relatorioDto.resumoAtividadesPesquisa(), 
            relatorioDto.declaracaoAdicionalParaCCP(), 
            relatorioDto.necessidadeApoioCoordenacao()
        );

        relatorio.setAluno(alunoRepository.findById(numeroUspLogado).get());
        relatorio.setDocente(docenteRepository.findById(relatorioDto.numeroUspParecista()).get());

        // Muda o status de entrega do relatório de PENDENTE para ENTREGUE
        String semestreAnoAtual = SemestreUtil.determinaSemestreAnoAtual();
        StatusEntregaRelatorio statusEntrega = statusEntregaRelatorioRepository
            .findByAlunoAndSemestreAno(alunoRepository.findById(numeroUspLogado).get(), semestreAnoAtual);

        if (statusEntrega != null) {
            statusEntrega.setSituacaoEntregaRelatorio(SituacaoEntregaRelatorio.ENTREGUE);
            statusEntregaRelatorioRepository.save(statusEntrega);
        }

        Relatorio relatorioSalvo = relatorioRepository.save(relatorio);

        // Publica o evento após salvar o relatório
        eventPublisher.publishEvent(new RelatorioCriadoEvent(relatorioSalvo));

        return relatorioSalvo.getId();
    }

    public Relatorio reenviarRelatorio(Long numeroUspAluno, Long relatorioId, RelatorioDto relatorioDto) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(numeroUspAluno);
        Optional<Relatorio> relatorioOptional = relatorioRepository.findById(relatorioId);
        if (alunoOptional.isEmpty() || relatorioOptional.isEmpty()) return null;

        if (!docenteRepository.existsById(relatorioDto.numeroUspParecista())) throw new RuntimeException("Parecista com número USP " + relatorioDto.numeroUspParecista() + " não encontrado.");

        Long numeroUspLogado = AuthenticatedUserUtil.getNumeroUspLogado();

        Relatorio relatorio = relatorioOptional.get();
        relatorio.setDataUltimaAtualizacaoLattes(relatorioDto.dataUltimaAtualizacaoLattes());
        relatorio.setTipoCurso(relatorioDto.tipoCurso());
        relatorio.setMesAnoIngressoComoAlunoRegular(relatorioDto.mesAnoIngressoComoAlunoRegular());
        relatorio.setResultadoUltimoRelatorio(relatorioDto.resultadoUltimoRelatorio());
        relatorio.setQuantidadeAprovacoesDesdeInicioCurso(relatorioDto.quantidadeAprovacoesDesdeInicioCurso());
        relatorio.setQuantidadeReprovacoes2023S2(relatorioDto.quantidadeReprovacoes2023S2());
        relatorio.setQuantidadeReprovacoesDesdeInicioCurso(relatorioDto.quantidadeReprovacoesDesdeInicioCurso());
        relatorio.setRealizouExameProficienciaLinguas(relatorioDto.realizouExameProficienciaLinguas());
        relatorio.setRealizouExameQualificacao(relatorioDto.realizouExameQualificacao());
        relatorio.setPrazoMaximoInscricaoExameQualificacao(relatorioDto.prazoMaximoInscricaoExameQualificacao());
        relatorio.setPrazoMaximoDepositoDissertacao(relatorioDto.prazoMaximoDepositoDissertacao());
        relatorio.setArtigosEmFaseEscrita(relatorioDto.artigosEmFaseEscrita());
        relatorio.setArtigosSubmetidosEEmPeriodoAvaliacao(relatorioDto.artigosSubmetidosEEmPeriodoAvaliacao());
        relatorio.setArtigosAceitosOuPublicados(relatorioDto.artigosAceitosOuPublicados());
        relatorio.setParticipacaoAtividadesAcademicas2024S1(relatorioDto.participacaoAtividadesAcademicas2024S1());
        relatorio.setResumoAtividadesPesquisa(relatorioDto.resumoAtividadesPesquisa());
        relatorio.setDeclaracaoAdicionalParaCCP(relatorioDto.declaracaoAdicionalParaCCP());
        relatorio.setNecessidadeApoioCoordenacao(relatorioDto.necessidadeApoioCoordenacao());

        String semestreAnoAtual = SemestreUtil.determinaSemestreAnoAtual();
        StatusEntregaRelatorio statusEntrega = statusEntregaRelatorioRepository
            .findByAlunoAndSemestreAno(alunoRepository.findById(numeroUspLogado).get(), semestreAnoAtual);

        if (statusEntrega != null) {
            statusEntrega.setSituacaoEntregaRelatorio(SituacaoEntregaRelatorio.ENTREGUE);
            statusEntrega.setNumeroDaEntrega(statusEntrega.getNumeroDaEntrega() + 1);

            statusEntregaRelatorioRepository.save(statusEntrega);
        }

        Relatorio relatorioSalvo = relatorioRepository.save(relatorio);

        // Publica o evento após salvar o relatório
        eventPublisher.publishEvent(new RelatorioCriadoEvent(relatorioSalvo));

        return relatorioSalvo;
    }
}

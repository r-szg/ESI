package br.usp.projetoa.service;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.usp.projetoa.enums.SituacaoEntregaRelatorio;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.StatusEntregaRelatorio;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.StatusEntregaRelatorioRepository;
import br.usp.projetoa.util.SemestreUtil;

@SpringBootTest(classes = StatusEntregaRelatorioService.class)
public class StatusEntregaRelatorioServiceTest {
    @MockBean private StatusEntregaRelatorioRepository statusEntregaRelatorioRepository;
    @MockBean private AlunoRepository alunoRepository;

    @Autowired private StatusEntregaRelatorioService statusEntregaRelatorioService;


    @Nested
    class gerarStatusEntregaRelatorioParaNovoSemestre {
        @Test
        @DisplayName("Deve gerar status da entrega do relatório para todos alunos quando iniciar novo semestre")
        void deveGerarStatusEntregaRelatorioParaTodosAlunosQuandoIniciarNovoSemestre() {
            Aluno aluno1 = new Aluno();
            Aluno aluno2 = new Aluno();

            StatusEntregaRelatorio statusEntregaRelatorio = new StatusEntregaRelatorio("2S2024", SituacaoEntregaRelatorio.PENDENTE);

            List<Aluno> listaAlunos = List.of(aluno1, aluno2);

            when(alunoRepository.findAll()).thenReturn(listaAlunos);
            when(statusEntregaRelatorioRepository.save(any(StatusEntregaRelatorio.class))).thenReturn(statusEntregaRelatorio);

            statusEntregaRelatorioService.gerarStatusEntregaRelatorioParaNovoSemestre();

            verify(statusEntregaRelatorioRepository, times(listaAlunos.size())).save(any(StatusEntregaRelatorio.class));
        }
    }


    @Nested
    class buscarTodosStatus {
        @Test
        @DisplayName("Deve buscar todos status com sucesso")
        void deveBuscarTodosStatusComSucesso() {
            StatusEntregaRelatorio statusEntregaRelatorio1 = new StatusEntregaRelatorio("2S2024", SituacaoEntregaRelatorio.PENDENTE);
            StatusEntregaRelatorio statusEntregaRelatorio2 = new StatusEntregaRelatorio("2S2024", SituacaoEntregaRelatorio.PENDENTE);

            List<StatusEntregaRelatorio> listaStatus = List.of(statusEntregaRelatorio1, statusEntregaRelatorio2);

            when(statusEntregaRelatorioRepository.findAll()).thenReturn(listaStatus);

            List<StatusEntregaRelatorio> output = statusEntregaRelatorioService.buscarTodosStatus();

            assertNotNull(output);
            assertEquals(listaStatus.size(), output.size());
        }
    }


    @Nested
    class buscarStatusPorAlunoNoSemestreAtual {
        @Test
        @DisplayName("Deve buscar o status de entrega do relatório do aluno no semestre atual quando existir")
        void deveBuscarStatusEntregaRelatorioDoAlunoNoSemestreAtualQuandoExistir() {
            MockedStatic<SemestreUtil> mockedSemestreUtil = Mockito.mockStatic(SemestreUtil.class);

            Aluno aluno1 = new Aluno();
            aluno1.setNumeroUsp(1L);

            Aluno aluno2 = new Aluno();
            aluno2.setNumeroUsp(2L);

            StatusEntregaRelatorio statusEntregaRelatorio1 = new StatusEntregaRelatorio("2S2024", SituacaoEntregaRelatorio.PENDENTE);
            statusEntregaRelatorio1.setAluno(aluno1);
            StatusEntregaRelatorio statusEntregaRelatorio2 = new StatusEntregaRelatorio("2S2020", SituacaoEntregaRelatorio.ENTREGUE);
            statusEntregaRelatorio2.setAluno(aluno2);

            List<StatusEntregaRelatorio> listaStatus = List.of(statusEntregaRelatorio1, statusEntregaRelatorio2);

            when(statusEntregaRelatorioRepository.findAll()).thenReturn(listaStatus);
            mockedSemestreUtil.when(SemestreUtil::determinaSemestreAnoAtual).thenReturn("2S2024");

            StatusEntregaRelatorio output = statusEntregaRelatorioService.buscarStatusPorAlunoNoSemestreAtual(1L);

            assertNotNull(output);
            assertEquals(statusEntregaRelatorio1.getSemestreAno(), output.getSemestreAno());
            assertEquals(statusEntregaRelatorio1.getSituacaoEntregaRelatorio(), output.getSituacaoEntregaRelatorio());

            mockedSemestreUtil.close();
        }

        @Test
        @DisplayName("Deve retornar null quando o status de entrega do relatório do aluno no semestre atual não existir")
        void deveRetornarNullQuandoStatusEntregaRelatorioDoAlunoNoSemestreAtualNaoExistir() {
            MockedStatic<SemestreUtil> mockedSemestreUtil = Mockito.mockStatic(SemestreUtil.class);

            Aluno aluno1 = new Aluno();
            aluno1.setNumeroUsp(1L);

            Aluno aluno2 = new Aluno();
            aluno2.setNumeroUsp(2L);

            StatusEntregaRelatorio statusEntregaRelatorio1 = new StatusEntregaRelatorio("2S2019", SituacaoEntregaRelatorio.PENDENTE);
            statusEntregaRelatorio1.setAluno(aluno1);
            StatusEntregaRelatorio statusEntregaRelatorio2 = new StatusEntregaRelatorio("2S2020", SituacaoEntregaRelatorio.ENTREGUE);
            statusEntregaRelatorio2.setAluno(aluno2);

            List<StatusEntregaRelatorio> listaStatus = List.of(statusEntregaRelatorio1, statusEntregaRelatorio2);

            when(statusEntregaRelatorioRepository.findAll()).thenReturn(listaStatus);
            mockedSemestreUtil.when(SemestreUtil::determinaSemestreAnoAtual).thenReturn("2S2024");

            StatusEntregaRelatorio output = statusEntregaRelatorioService.buscarStatusPorAlunoNoSemestreAtual(1L);

            assertNull(output);

            mockedSemestreUtil.close();
        }
    }
}

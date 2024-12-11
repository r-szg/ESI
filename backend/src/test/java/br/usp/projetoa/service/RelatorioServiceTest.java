package br.usp.projetoa.service;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import br.usp.projetoa.dto.RelatorioDto;
import br.usp.projetoa.enums.Quantidade;
import br.usp.projetoa.enums.SituacaoEntregaRelatorio;
import br.usp.projetoa.enums.SituacaoExame;
import br.usp.projetoa.enums.SituacaoRelatorio;
import br.usp.projetoa.enums.TipoCurso;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.model.Relatorio;
import br.usp.projetoa.model.StatusEntregaRelatorio;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DocenteRepository;
import br.usp.projetoa.repository.RelatorioRepository;
import br.usp.projetoa.repository.StatusEntregaRelatorioRepository;
import br.usp.projetoa.security.AuthenticatedUserUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest(classes = RelatorioService.class)
public class RelatorioServiceTest {
    @MockBean private RelatorioRepository relatorioRepository;
    @MockBean private AlunoRepository alunoRepository;
    @MockBean private DocenteRepository docenteRepository;
    @MockBean private StatusEntregaRelatorioRepository statusEntregaRelatorioRepository;
    @MockBean private ApplicationEventPublisher eventPublisher;

    @Autowired private RelatorioService relatorioService;


    @Nested
    class buscarTodosRelatorios {
        @Test
        @DisplayName("Deve buscar todos relatórios com sucesso")
        void deveBuscarTodosRelatoriosComSucesso() {
            Relatorio relatorio = new Relatorio();

            List<Relatorio> listaRelatorios = List.of(relatorio);

            when(relatorioRepository.findAll()).thenReturn(listaRelatorios);

            List<Relatorio> output = relatorioService.buscarTodosRelatorios();

            assertNotNull(output);
            assertEquals(listaRelatorios.size(), output.size());
        }
    }

    
    @Nested
    class buscarRelatoriosPorAluno {
        @Test
        @DisplayName("Deve buscar todos relatórios de um aluno com sucesso")
        void deveBuscarTodosRelatoriosDeUmAlunoComSucesso() {
            Long numeroUsp = 1L;

            Aluno aluno1 = new Aluno();
            aluno1.setNumeroUsp(1L);
            Aluno aluno2 = new Aluno();
            aluno2.setNumeroUsp(2L);

            Relatorio relatorio1 = new Relatorio();
            relatorio1.setAluno(aluno1);
            Relatorio relatorio2 = new Relatorio();
            relatorio2.setAluno(aluno2);

            List<Relatorio> todosRelatorios = List.of(relatorio1, relatorio2);

            when(relatorioRepository.findAll()).thenReturn(todosRelatorios);

            List<Relatorio> output = relatorioService.buscarRelatoriosPorAluno(numeroUsp);

            assertNotNull(output);
            assertEquals(1, output.size());
        }
    }

    
    @Nested
    class buscarRelatoriosPorOrientador {
        @Test
        @DisplayName("Deve buscar todos relatórios de um orientador com sucesso")
        void deveBuscarTodosRelatoriosDeUmOrientadorComSucesso() {
            Long numeroUsp = 1L;

            Docente docente1 = new Docente();
            docente1.setNumeroUsp(1L);
            docente1.setEhOrientador(true);
            Docente docente2 = new Docente();
            docente2.setNumeroUsp(2L);
            docente2.setEhOrientador(true);

            Relatorio relatorio1 = new Relatorio();
            relatorio1.setDocente(docente1);
            Relatorio relatorio2 = new Relatorio();
            relatorio2.setDocente(docente2);

            List<Relatorio> todosRelatorios = List.of(relatorio1, relatorio2);

            when(relatorioRepository.findAll()).thenReturn(todosRelatorios);

            List<Relatorio> output = relatorioService.buscarRelatoriosPorOrientador(numeroUsp);

            assertNotNull(output);
            assertEquals(1, output.size());
        }
    }

    
    @Nested
    class criarRelatorio {
        @Test
        @DisplayName("Deve criar um relatório com sucesso")
        void deveCriarRelatorioComSucesso() {
            MockedStatic<AuthenticatedUserUtil> mockedAuthenticatedUserUtil = Mockito.mockStatic(AuthenticatedUserUtil.class);

            StatusEntregaRelatorio statusEntregaRelatorio = new StatusEntregaRelatorio("2S2024", SituacaoEntregaRelatorio.PENDENTE);

            Relatorio relatorio = new Relatorio(
                LocalDate.now(), 
                TipoCurso.MESTRADO, 
                LocalDate.now(), 
                SituacaoRelatorio.NAO_SE_APLICA, 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                SituacaoExame.NAO_REALIZADO, 
                SituacaoExame.APROVADO, 
                LocalDate.now(), 
                LocalDate.now(), 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                "participacaoAtividadesAcademicas2024S1", 
                "resumoAtividadesPesquisa", 
                "declaracaoAdicionalParaCCP", 
                false
            );
            relatorio.setId(1L);

            RelatorioDto relatorioDto = new RelatorioDto(
                1L,
                LocalDate.now(), 
                TipoCurso.MESTRADO, 
                LocalDate.now(), 
                SituacaoRelatorio.NAO_SE_APLICA, 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                SituacaoExame.NAO_REALIZADO, 
                SituacaoExame.APROVADO, 
                LocalDate.now(), 
                LocalDate.now(), 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                "participacaoAtividadesAcademicas2024S1", 
                "resumoAtividadesPesquisa", 
                "declaracaoAdicionalParaCCP", 
                false
            );

            mockedAuthenticatedUserUtil.when(AuthenticatedUserUtil::getNumeroUspLogado).thenReturn(1L);
            when(alunoRepository.existsById(anyLong())).thenReturn(true);
            when(docenteRepository.existsById(anyLong())).thenReturn(true);

            when(alunoRepository.findById(anyLong())).thenReturn(Optional.of(new Aluno()));
            when(docenteRepository.findById(anyLong())).thenReturn(Optional.of(new Docente()));

            when(statusEntregaRelatorioRepository.findByAlunoAndSemestreAno(any(), any())).thenReturn(statusEntregaRelatorio);
            when(statusEntregaRelatorioRepository.save(any(StatusEntregaRelatorio.class))).thenReturn(statusEntregaRelatorio);

            when(relatorioRepository.save(any(Relatorio.class))).thenReturn(relatorio);

            Long output = relatorioService.criarRelatorio(relatorioDto);

            assertNotNull(output);
            assertEquals(relatorio.getId(), output);
            verify(relatorioRepository, times(1)).save(any(Relatorio.class));

            mockedAuthenticatedUserUtil.close();
        }

        @Test
        @DisplayName("Deve lançar exceção quando aluno não existir")
        void deveLancarExcecaoQuandoAlunoNaoExistir() {
            MockedStatic<AuthenticatedUserUtil> mockedAuthenticatedUserUtil = Mockito.mockStatic(AuthenticatedUserUtil.class);

            RelatorioDto relatorioDto = new RelatorioDto(
                1L,
                LocalDate.now(), 
                TipoCurso.MESTRADO, 
                LocalDate.now(), 
                SituacaoRelatorio.NAO_SE_APLICA, 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                SituacaoExame.NAO_REALIZADO, 
                SituacaoExame.APROVADO, 
                LocalDate.now(), 
                LocalDate.now(), 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                "participacaoAtividadesAcademicas2024S1", 
                "resumoAtividadesPesquisa", 
                "declaracaoAdicionalParaCCP", 
                false
            );

            mockedAuthenticatedUserUtil.when(AuthenticatedUserUtil::getNumeroUspLogado).thenReturn(1L);
            when(alunoRepository.existsById(anyLong())).thenReturn(false);

            assertThrows(RuntimeException.class, () -> relatorioService.criarRelatorio(relatorioDto));

            mockedAuthenticatedUserUtil.close();
        }

        @Test
        @DisplayName("Deve lançar exceção quando parecista não existir")
        void deveLancarExcecaoQuandoParecistaNaoExistir() {
            MockedStatic<AuthenticatedUserUtil> mockedAuthenticatedUserUtil = Mockito.mockStatic(AuthenticatedUserUtil.class);

            RelatorioDto relatorioDto = new RelatorioDto(
                1L,
                LocalDate.now(), 
                TipoCurso.MESTRADO, 
                LocalDate.now(), 
                SituacaoRelatorio.NAO_SE_APLICA, 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                SituacaoExame.NAO_REALIZADO, 
                SituacaoExame.APROVADO, 
                LocalDate.now(), 
                LocalDate.now(), 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                "participacaoAtividadesAcademicas2024S1", 
                "resumoAtividadesPesquisa", 
                "declaracaoAdicionalParaCCP", 
                false
            );

            mockedAuthenticatedUserUtil.when(AuthenticatedUserUtil::getNumeroUspLogado).thenReturn(1L);
            when(alunoRepository.existsById(anyLong())).thenReturn(true);
            when(docenteRepository.existsById(anyLong())).thenReturn(false);

            assertThrows(RuntimeException.class, () -> relatorioService.criarRelatorio(relatorioDto));

            mockedAuthenticatedUserUtil.close();
        }
    }

    
    @Nested
    class reenviarRelatorio {
        @Test
        @DisplayName("Deve reenviar o relatório com sucesso")
        void deveReenviarRelatorioComSucesso() {
            MockedStatic<AuthenticatedUserUtil> mockedAuthenticatedUserUtil = Mockito.mockStatic(AuthenticatedUserUtil.class);

            StatusEntregaRelatorio statusEntregaRelatorio = new StatusEntregaRelatorio("2S2024", SituacaoEntregaRelatorio.PENDENTE);

            Relatorio relatorio = new Relatorio(
                LocalDate.now(), 
                TipoCurso.MESTRADO, 
                LocalDate.now(), 
                SituacaoRelatorio.NAO_SE_APLICA, 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                SituacaoExame.NAO_REALIZADO, 
                SituacaoExame.APROVADO, 
                LocalDate.now(), 
                LocalDate.now(), 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                "participacaoAtividadesAcademicas2024S1", 
                "resumoAtividadesPesquisa", 
                "declaracaoAdicionalParaCCP", 
                false
            );
            relatorio.setId(1L);

            RelatorioDto relatorioAtualizadoDto = new RelatorioDto(
                1L,
                LocalDate.now(), 
                TipoCurso.DOUTORADO,
                LocalDate.now(), 
                SituacaoRelatorio.ADEQUADO, 
                Quantidade.TRES, 
                Quantidade.DOIS, 
                Quantidade.ZERO, 
                SituacaoExame.REPROVADO, 
                SituacaoExame.NAO_REALIZADO, 
                LocalDate.now(), 
                LocalDate.now(), 
                Quantidade.UM, 
                Quantidade.TRES, 
                Quantidade.DOIS, 
                "participacaoAtividadesAcademicas2024S1Atualizado", 
                "resumoAtividadesPesquisaAtualizado", 
                "declaracaoAdicionalParaCCPAtualizado", 
                true
            );

            when(alunoRepository.findById(anyLong())).thenReturn(Optional.of(new Aluno()));
            when(relatorioRepository.findById(anyLong())).thenReturn(Optional.of(relatorio));
            when(docenteRepository.existsById(anyLong())).thenReturn(true);
            mockedAuthenticatedUserUtil.when(AuthenticatedUserUtil::getNumeroUspLogado).thenReturn(1L);

            when(statusEntregaRelatorioRepository.findByAlunoAndSemestreAno(any(), any())).thenReturn(statusEntregaRelatorio);
            when(statusEntregaRelatorioRepository.save(any(StatusEntregaRelatorio.class))).thenReturn(statusEntregaRelatorio);

            when(relatorioRepository.save(any(Relatorio.class))).thenReturn(relatorio);

            Relatorio relatorioAtualizado = relatorioService.reenviarRelatorio(1L, 1L, relatorioAtualizadoDto);

            assertNotNull(relatorioAtualizado);
            assertEquals(relatorioAtualizadoDto.dataUltimaAtualizacaoLattes(), relatorioAtualizado.getDataUltimaAtualizacaoLattes());
            assertEquals(relatorioAtualizadoDto.tipoCurso(), relatorioAtualizado.getTipoCurso());
            assertEquals(relatorioAtualizadoDto.mesAnoIngressoComoAlunoRegular(), relatorioAtualizado.getMesAnoIngressoComoAlunoRegular());
            assertEquals(relatorioAtualizadoDto.resultadoUltimoRelatorio(), relatorioAtualizado.getResultadoUltimoRelatorio());
            assertEquals(relatorioAtualizadoDto.quantidadeAprovacoesDesdeInicioCurso(), relatorioAtualizado.getQuantidadeAprovacoesDesdeInicioCurso());
            assertEquals(relatorioAtualizadoDto.quantidadeReprovacoes2023S2(), relatorioAtualizado.getQuantidadeReprovacoes2023S2());
            assertEquals(relatorioAtualizadoDto.quantidadeReprovacoesDesdeInicioCurso(), relatorioAtualizado.getQuantidadeReprovacoesDesdeInicioCurso());
            assertEquals(relatorioAtualizadoDto.realizouExameProficienciaLinguas(), relatorioAtualizado.getRealizouExameProficienciaLinguas());
            assertEquals(relatorioAtualizadoDto.realizouExameQualificacao(), relatorioAtualizado.getRealizouExameQualificacao());
            assertEquals(relatorioAtualizadoDto.prazoMaximoInscricaoExameQualificacao(), relatorioAtualizado.getPrazoMaximoInscricaoExameQualificacao());
            assertEquals(relatorioAtualizadoDto.prazoMaximoDepositoDissertacao(), relatorioAtualizado.getPrazoMaximoDepositoDissertacao());
            assertEquals(relatorioAtualizadoDto.artigosEmFaseEscrita(), relatorioAtualizado.getArtigosEmFaseEscrita());
            assertEquals(relatorioAtualizadoDto.artigosSubmetidosEEmPeriodoAvaliacao(), relatorioAtualizado.getArtigosSubmetidosEEmPeriodoAvaliacao());
            assertEquals(relatorioAtualizadoDto.artigosAceitosOuPublicados(), relatorioAtualizado.getArtigosAceitosOuPublicados());
            assertEquals(relatorioAtualizadoDto.participacaoAtividadesAcademicas2024S1(), relatorioAtualizado.getParticipacaoAtividadesAcademicas2024S1());
            assertEquals(relatorioAtualizadoDto.resumoAtividadesPesquisa(), relatorioAtualizado.getResumoAtividadesPesquisa());
            assertEquals(relatorioAtualizadoDto.declaracaoAdicionalParaCCP(), relatorioAtualizado.getDeclaracaoAdicionalParaCCP());
            assertEquals(relatorioAtualizadoDto.necessidadeApoioCoordenacao(), relatorioAtualizado.isNecessidadeApoioCoordenacao());

            verify(relatorioRepository, times(1)).save(any(Relatorio.class));

            mockedAuthenticatedUserUtil.close();
        }

        @Test
        @DisplayName("Deve retornar null quando aluno não existir")
        void deveRetornarNullQuandoAlunoNaoExistir() {
            RelatorioDto relatorioDto = new RelatorioDto(
                1L,
                LocalDate.now(), 
                TipoCurso.MESTRADO, 
                LocalDate.now(), 
                SituacaoRelatorio.NAO_SE_APLICA, 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                SituacaoExame.NAO_REALIZADO, 
                SituacaoExame.APROVADO, 
                LocalDate.now(), 
                LocalDate.now(), 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                "participacaoAtividadesAcademicas2024S1", 
                "resumoAtividadesPesquisa", 
                "declaracaoAdicionalParaCCP", 
                false
            );

            when(alunoRepository.findById(anyLong())).thenReturn(Optional.empty());
            when(relatorioRepository.findById(anyLong())).thenReturn(Optional.of(new Relatorio()));

            Relatorio output = relatorioService.reenviarRelatorio(1L, 1L, relatorioDto);

            assertNull(output);
        }

        @Test
        @DisplayName("Deve retornar null quando relatório não existir")
        void deveRetornarNullQuandoRelatorioNaoExistir() {
            RelatorioDto relatorioDto = new RelatorioDto(
                1L,
                LocalDate.now(), 
                TipoCurso.MESTRADO, 
                LocalDate.now(), 
                SituacaoRelatorio.NAO_SE_APLICA, 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                SituacaoExame.NAO_REALIZADO, 
                SituacaoExame.APROVADO, 
                LocalDate.now(), 
                LocalDate.now(), 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                "participacaoAtividadesAcademicas2024S1", 
                "resumoAtividadesPesquisa", 
                "declaracaoAdicionalParaCCP", 
                false
            );

            when(alunoRepository.findById(anyLong())).thenReturn(Optional.of(new Aluno()));
            when(relatorioRepository.findById(anyLong())).thenReturn(Optional.empty());

            Relatorio output = relatorioService.reenviarRelatorio(1L, 1L, relatorioDto);

            assertNull(output);
        }

        @Test
        @DisplayName("Deve lançar exceção quando docente não existir")
        void deveLancarExcecaoQuandoDocenteNaoExistir() {
            RelatorioDto relatorioDto = new RelatorioDto(
                1L,
                LocalDate.now(), 
                TipoCurso.MESTRADO, 
                LocalDate.now(), 
                SituacaoRelatorio.NAO_SE_APLICA, 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                SituacaoExame.NAO_REALIZADO, 
                SituacaoExame.APROVADO, 
                LocalDate.now(), 
                LocalDate.now(), 
                Quantidade.DOIS, 
                Quantidade.UM, 
                Quantidade.TRES, 
                "participacaoAtividadesAcademicas2024S1", 
                "resumoAtividadesPesquisa", 
                "declaracaoAdicionalParaCCP", 
                false
            );

            when(alunoRepository.findById(anyLong())).thenReturn(Optional.of(new Aluno()));
            when(relatorioRepository.findById(anyLong())).thenReturn(Optional.of(new Relatorio()));
            when(docenteRepository.existsById(anyLong())).thenReturn(false);

            assertThrows(RuntimeException.class, () -> relatorioService.reenviarRelatorio(1L, 1L, relatorioDto));
        }
    }
}

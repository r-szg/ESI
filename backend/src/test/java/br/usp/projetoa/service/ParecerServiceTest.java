package br.usp.projetoa.service;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import br.usp.projetoa.dto.ParecerDto;
import br.usp.projetoa.enums.SituacaoRelatorio;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.model.Parecer;
import br.usp.projetoa.model.Relatorio;
import br.usp.projetoa.repository.DocenteRepository;
import br.usp.projetoa.repository.ParecerRepository;
import br.usp.projetoa.repository.RelatorioRepository;
import br.usp.projetoa.security.AuthenticatedUserUtil;

@SpringBootTest(classes = ParecerService.class)
public class ParecerServiceTest {
    @MockBean private ParecerRepository parecerRepository;
    @MockBean private RelatorioRepository relatorioRepository;
    @MockBean private DocenteRepository docenteRepository;
    @MockBean private ApplicationEventPublisher eventPublisher;

    @Autowired private ParecerService parecerService;


    @Nested
    class criarParecer {
        @Test
        @DisplayName("Deve criar um parecer com sucesso")
        void deveCriarParecerComSucesso() {
            MockedStatic<AuthenticatedUserUtil> mockedAuthenticatedUserUtil = Mockito.mockStatic(AuthenticatedUserUtil.class);

            String parecerTexto = "parecer";
            SituacaoRelatorio desempenhoRelatorio = SituacaoRelatorio.ADEQUADO;

            Relatorio relatorio = new Relatorio();
            Docente docente = new Docente(1L, "nomeCompleto", "senha", "email@email.com", false, true);

            Parecer parecer = new Parecer(parecerTexto, desempenhoRelatorio);
            parecer.setId(1L);

            ParecerDto parecerDto = new ParecerDto(1L, parecerTexto, desempenhoRelatorio);

            when(relatorioRepository.findById(anyLong())).thenReturn(Optional.of(relatorio));
            mockedAuthenticatedUserUtil.when(AuthenticatedUserUtil::getNumeroUspLogado).thenReturn(1L);
            when(docenteRepository.findById(anyLong())).thenReturn(Optional.of(docente));
            when(parecerRepository.save(any(Parecer.class))).thenReturn(parecer);

            Long output = parecerService.criarParecer(parecerDto);

            assertNotNull(output);
            assertEquals(parecer.getId(), output);
            verify(parecerRepository, times(1)).save(any(Parecer.class));

            mockedAuthenticatedUserUtil.close();
        }

        @Test
        @DisplayName("Deve lançar exceção quando o usuário não estiver autenticado")
        void deveLancarExcecaoQuandoUsuarioNaoEstiverAutenticado() {
            MockedStatic<AuthenticatedUserUtil> mockedAuthenticatedUserUtil = Mockito.mockStatic(AuthenticatedUserUtil.class);

            ParecerDto parecerDto = new ParecerDto(1L, "parecer", SituacaoRelatorio.ADEQUADO);
            Relatorio relatorio = new Relatorio();

            when(relatorioRepository.findById(anyLong())).thenReturn(Optional.of(relatorio));
            mockedAuthenticatedUserUtil.when(AuthenticatedUserUtil::getNumeroUspLogado).thenThrow(IllegalStateException.class);

            assertThrows(IllegalStateException.class, () -> parecerService.criarParecer(parecerDto));

            mockedAuthenticatedUserUtil.close();
        }

        @Test
        @DisplayName("Deve retonar null quando o relatório não existir")
        void deveRetornarNullQuandoRelatorioNaoExistir() {
            ParecerDto parecerDto = new ParecerDto(1L, "parecer", SituacaoRelatorio.ADEQUADO);

            when(relatorioRepository.findById(anyLong())).thenReturn(Optional.empty());

            Long output = parecerService.criarParecer(parecerDto);

            assertNull(output);
        }

        @Test
        @DisplayName("Deve retonar null quando o docente não existir")
        void deveRetornarNullQuandoDocenteNaoExistir() {
            MockedStatic<AuthenticatedUserUtil> mockedAuthenticatedUserUtil = Mockito.mockStatic(AuthenticatedUserUtil.class);
            ;
            String parecerTexto = "parecer";
            SituacaoRelatorio desempenhoRelatorio = SituacaoRelatorio.ADEQUADO;

            Relatorio relatorio = new Relatorio();
            Parecer parecer = new Parecer(parecerTexto, desempenhoRelatorio);
            parecer.setId(1L);

            ParecerDto parecerDto = new ParecerDto(1L, parecerTexto, desempenhoRelatorio);

            when(relatorioRepository.findById(anyLong())).thenReturn(Optional.of(relatorio));
            mockedAuthenticatedUserUtil.when(AuthenticatedUserUtil::getNumeroUspLogado).thenReturn(1L);
            when(docenteRepository.findById(anyLong())).thenReturn(Optional.empty());

            Long output = parecerService.criarParecer(parecerDto);

            assertNull(output);

            mockedAuthenticatedUserUtil.close();
        }
    }

    
    @Nested
    class atualizarParecer {
        @Test
        @DisplayName("Deve atualizar um parecer com sucesso")
        void deveAtualizarParecerComSucesso() {
            Parecer parecer = new Parecer("parecer", SituacaoRelatorio.INADEQUADO);
            parecer.setId(1L);

            ParecerDto parecerAtualizadoDto = new ParecerDto(1L, "parecerAtualizado", SituacaoRelatorio.ADEQUADO);

            when(parecerRepository.findById(parecer.getId())).thenReturn(Optional.of(parecer));
            when(parecerRepository.save(any(Parecer.class))).thenReturn(parecer);

            Parecer parecerAtualizado = parecerService.atualizarParecer(parecer.getId(), parecerAtualizadoDto);

            assertNotNull(parecerAtualizado);
            assertEquals(parecerAtualizadoDto.parecer(), parecerAtualizado.getParecer());
            assertEquals(parecerAtualizadoDto.desempenhoRelatorio(), parecerAtualizado.getDesempenhoRelatorio());

            verify(parecerRepository, times(1)).save(any(Parecer.class));
        }

        @Test
        @DisplayName("Deve retornar null quando parecer não existir")
        void deveRetornarNullQuandoParecerNaoExistir() {
            Long parecerId = 1L;
            ParecerDto parecerDto = new ParecerDto(1L, "parecer", SituacaoRelatorio.ADEQUADO);

            when(parecerRepository.findById(anyLong())).thenReturn(Optional.empty());

            Parecer output = parecerService.atualizarParecer(parecerId, parecerDto);

            assertNull(output);
        }
    }


    @Nested
    class deletarParecer {
        @Test
        @DisplayName("Deve deletar um parecer com sucesso")
        void deveDeletarParecerComSucesso() {
            Long parecerId = 1L;

            when(parecerRepository.existsById(parecerId)).thenReturn(true);

            boolean parecerDeletado = parecerService.deletarParecer(parecerId);

            assertEquals(true, parecerDeletado);
            verify(parecerRepository, times(1)).deleteById(parecerId);
        }

        @Test
        @DisplayName("Deve retornar false quando parecer não existir")
        void deveRetornarFalseQuandoParecerNaoExistir() {
            Long parecerId = 1L;

            when(parecerRepository.existsById(parecerId)).thenReturn(false);

            boolean parecerDeletado = parecerService.deletarParecer(parecerId);

            assertEquals(false, parecerDeletado);
        }
    }
}

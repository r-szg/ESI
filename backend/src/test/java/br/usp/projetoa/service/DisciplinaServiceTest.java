package br.usp.projetoa.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.usp.projetoa.dto.DisciplinaDto;
import br.usp.projetoa.exception.EntidadeDuplicadaException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.usp.projetoa.model.Disciplina;
import br.usp.projetoa.repository.DisciplinaRepository;

@SpringBootTest(classes = DisciplinaService.class)
public class DisciplinaServiceTest {
    @MockBean private DisciplinaRepository disciplinaRepository;

    @Autowired private DisciplinaService disciplinaService;

    @Nested
    class buscarTodasDisciplinas {
        @Test
        @DisplayName("Deve buscar todas as disciplinas com sucesso")
        void deveBuscarTodasDisciplinasComSucesso() {
            Disciplina disciplina = new Disciplina("codigoDisciplina", "nomeDisciplina");

            List<Disciplina> listaDisciplinas = List.of(disciplina);

            when(disciplinaRepository.findAll()).thenReturn(listaDisciplinas);

            List<Disciplina> output = disciplinaService.buscarTodasDisciplinas();

            assertNotNull(output);
            assertEquals(listaDisciplinas.size(), output.size());
        }
    }


    @Nested
    class buscarDisciplina {
        @Test
        @DisplayName("Deve buscar a disciplina pelo código com sucesso quando existir")
        void deveBuscarDisciplinaPorCodigoComSucessoQuandoOptionalEstaPresente() {
            Disciplina disciplina = new Disciplina("codigoDisciplina", "nomeDisciplina");

            when(disciplinaRepository.findById(disciplina.getCodigoDisciplina())).thenReturn(Optional.of(disciplina));

            Optional<Disciplina> disciplinaOptional = disciplinaService.buscarDisciplina(disciplina.getCodigoDisciplina());

            assertEquals(true, disciplinaOptional.isPresent());
        }

        @Test
        @DisplayName("Deve buscar a disciplina pelo código com sucesso quando não existir")
        void deveBuscarDisciplinaPorCodigoComSucessoQuandoOptionalEstaVazio() {
            String codigoDisciplina = "codigo";

            when(disciplinaRepository.findById(codigoDisciplina)).thenReturn(Optional.empty());

            Optional<Disciplina> disciplinaOptional = disciplinaService.buscarDisciplina(codigoDisciplina);

            assertEquals(true, disciplinaOptional.isEmpty());
        }
    }


    @Nested
    class criarDisciplina {
        @Test
        @DisplayName("Deve criar uma disciplina com sucesso")
        void deveCriarDisciplinaComSucesso() {
            Disciplina disciplina = new Disciplina("codigoDisciplina", "nomeDisciplina");

            DisciplinaDto disciplinaDto = new DisciplinaDto("codigoDisciplina", "nomeDisciplina");

            when(disciplinaRepository.existsById(disciplinaDto.codigoDisciplina())).thenReturn(false);
            when(disciplinaRepository.save(any(Disciplina.class))).thenReturn(disciplina);

            String output = disciplinaService.criarDisciplina(disciplinaDto);

            assertNotNull(output);
            assertEquals(disciplina.getCodigoDisciplina(), output);
            verify(disciplinaRepository, times(1)).save(any(Disciplina.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando código da disciplina já estiver cadastrado")
        void deveLancarExcecaoQuandoCodigoDaDisciplinaJaEstiverCadastrado() {
            DisciplinaDto disciplinaDto = new DisciplinaDto("codigoDisciplina", "nomeDisciplina");

            when(disciplinaRepository.existsById(disciplinaDto.codigoDisciplina())).thenReturn(true);

            assertThrows(EntidadeDuplicadaException.class, () -> disciplinaService.criarDisciplina(disciplinaDto));
        }
    }


    @Nested
    class atualizarDisciplina {
        @Test
        @DisplayName("Deve atualizar uma disciplina com sucesso")
        void deveAtualizarDisciplinaComSucesso() {
            Disciplina disciplina = new Disciplina("codigoDisciplina", "nomeDisciplina");

            DisciplinaDto disciplinaAtualizadaDto = new DisciplinaDto("codigoDisciplinaAtualizado", 
                "nomeDisciplinaAtualizado");

            when(disciplinaRepository.findById(disciplina.getCodigoDisciplina())).thenReturn(Optional.of(disciplina));
            when(disciplinaRepository.save(any(Disciplina.class))).thenReturn(disciplina);

            Disciplina disciplinaAtualizada = disciplinaService.atualizarDisciplina(disciplina.getCodigoDisciplina(), 
                disciplinaAtualizadaDto);

            assertNotNull(disciplinaAtualizada);
            assertEquals(disciplinaAtualizadaDto.codigoDisciplina(), disciplinaAtualizada.getCodigoDisciplina());
            assertEquals(disciplinaAtualizadaDto.nomeDisciplina(), disciplinaAtualizada.getNomeDisciplina());

            verify(disciplinaRepository, times(1)).save(any(Disciplina.class));
        }

        @Test
        @DisplayName("Deve retornar null quando disciplina não existir")
        void deveRetornarNullQuandoDisciplinaNaoExistir() {
            DisciplinaDto disciplinaDto = new DisciplinaDto("codigoDisciplina", "nomeDisciplina");

            when(disciplinaRepository.findById(anyString())).thenReturn(Optional.empty());

            assertNull(disciplinaService.atualizarDisciplina(disciplinaDto.codigoDisciplina(), disciplinaDto));
        }
    }


    @Nested
    class deletarDisciplina {
        @Test
        @DisplayName("Deve deletar uma disciplina com sucesso")
        void deveDeletarDisciplinaComSucesso() {
            String codigoDisciplina = "codigo";

            when(disciplinaRepository.existsById(codigoDisciplina)).thenReturn(true);

            boolean disciplinaDeletada = disciplinaService.deletarDisciplina(codigoDisciplina);

            assertEquals(true, disciplinaDeletada);
        }

        @Test
        @DisplayName("Deve retornar false quando a disciplina não existir")
        void deveRetornarFalseQuandoADisciplinaNaoExistir() {
            String codigoDisciplina = "codigo";

            when(disciplinaRepository.existsById(anyString())).thenReturn(false);

            boolean disciplinaDeletada = disciplinaService.deletarDisciplina(codigoDisciplina);

            assertEquals(false, disciplinaDeletada);
        }
    }
}

package br.usp.projetoa.service;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.usp.projetoa.dto.MatriculaDto;
import br.usp.projetoa.enums.SituacaoDisciplina;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.Disciplina;
import br.usp.projetoa.model.Matricula;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DisciplinaRepository;
import br.usp.projetoa.repository.MatriculaRepository;

@SpringBootTest(classes = MatriculaService.class)
public class MatriculaServiceTest {
    @MockBean private MatriculaRepository matriculaRepository;
    @MockBean private AlunoRepository alunoRepository;
    @MockBean private DisciplinaRepository disciplinaRepository;
    
    @Autowired private MatriculaService matriculaService;

    @Nested
    class buscarTodasMatriculas {
        @Test
        @DisplayName("Deve buscar todas as matrículas com sucesso")
        void deveBuscarTodasMatriculasComSucesso() {
            Matricula matricula1 = new Matricula(new Aluno(), new Disciplina(), SituacaoDisciplina.APROVADO);
            Matricula matricula2 = new Matricula(new Aluno(), new Disciplina(), SituacaoDisciplina.APROVADO);

            List<Matricula> listaMatriculas = List.of(matricula1, matricula2);

            when(matriculaRepository.findAll()).thenReturn(listaMatriculas);

            List<Matricula> output = matriculaService.buscarTodasMatriculas();

            assertNotNull(output);
            assertEquals(listaMatriculas.size(), output.size());
        }
    }


    @Nested
    class matricularAluno {
        @Test
        @DisplayName("Deve matricular o aluno com sucesso")
        void deveMatricularAlunoComSucesso() {
            Aluno aluno = new Aluno();
            Disciplina disciplina = new Disciplina();

            MatriculaDto matriculaDto = new MatriculaDto(1L, "codigoDisciplina", SituacaoDisciplina.APROVADO);

            Matricula matricula = new Matricula(aluno, disciplina, SituacaoDisciplina.APROVADO);

            when(alunoRepository.findById(anyLong())).thenReturn(Optional.of(aluno));
            when(disciplinaRepository.findById(anyString())).thenReturn(Optional.of(disciplina));
            when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

            Matricula output = matriculaService.matricularAluno(matriculaDto);

            assertNotNull(output);
        }

        @Test
        @DisplayName("Deve lançar exceção quando aluno não existir")
        void deveLancarExcecaoQuandoAlunoNaoExistir() {
            MatriculaDto matriculaDto = new MatriculaDto(1L, "codigoDisciplina", SituacaoDisciplina.APROVADO);

            when(alunoRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> matriculaService.matricularAluno(matriculaDto));
        }

        @Test
        @DisplayName("Deve lançar exceção quando disciplina não existir")
        void deveLancarExcecaoQuandoDisciplinaNaoExistir() {
            MatriculaDto matriculaDto = new MatriculaDto(1L, "codigoDisciplina", SituacaoDisciplina.APROVADO);

            when(alunoRepository.findById(anyLong())).thenReturn(Optional.of(new Aluno()));
            when(disciplinaRepository.findById(anyString())).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> matriculaService.matricularAluno(matriculaDto));
        }
    }


    @Nested
    class buscarMatriculasPorAluno {
        @Test
        @DisplayName("Deve buscar todas as matrículas de um aluno quando existir")
        void deveBuscarTodasMatriculasDoAlunoQuandoExistir() {
            Aluno aluno = new Aluno();
            aluno.setNumeroUsp(1L);

            Disciplina disciplina1 = new Disciplina("d1", "d1");
            Disciplina disciplina2 = new Disciplina("d2", "d2");

            Matricula matricula1 = new Matricula(aluno, disciplina1, SituacaoDisciplina.APROVADO);
            Matricula matricula2 = new Matricula(aluno, disciplina2, SituacaoDisciplina.REPROVADO);

            List<Matricula> matriculas = List.of(matricula1, matricula2);

            when(matriculaRepository.findByAluno_NumeroUsp(anyLong())).thenReturn(matriculas);

            List<Matricula> output = matriculaService.buscarMatriculasPorAluno(aluno.getNumeroUsp());

            assertNotNull(output);
            assertEquals(matriculas.size(), output.size());

            Matricula outputMatricula1 = output.get(0);
            assertEquals(matricula1.getAluno(), outputMatricula1.getAluno());
            assertEquals(matricula1.getDisciplina(), outputMatricula1.getDisciplina());
            assertEquals(matricula1.getSituacaoDisciplina(), outputMatricula1.getSituacaoDisciplina());

            Matricula outputMatricula2 = output.get(1);
            assertEquals(matricula2.getAluno(), outputMatricula2.getAluno());
            assertEquals(matricula2.getDisciplina(), outputMatricula2.getDisciplina());
            assertEquals(matricula2.getSituacaoDisciplina(), outputMatricula2.getSituacaoDisciplina());

            verify(matriculaRepository, times(1)).findByAluno_NumeroUsp(anyLong());
        }
    }
}

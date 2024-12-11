package br.usp.projetoa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.usp.projetoa.dto.AlunoDto;
import br.usp.projetoa.enums.TipoCurso;
import br.usp.projetoa.exception.EntidadeDuplicadaException;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DocenteRepository;
import br.usp.projetoa.repository.RoleRepository;
import br.usp.projetoa.repository.StatusEntregaRelatorioRepository;

@SpringBootTest(classes = AlunoService.class)
public class AlunoServiceTest {

    @MockBean private AlunoRepository alunoRepository;
    @MockBean private DocenteRepository docenteRepository;
    @MockBean private RoleRepository roleRepository;
    @MockBean private StatusEntregaRelatorioRepository statusEntregaRelatorioRepository;
    @MockBean private PasswordEncoder passwordEncoder;

    @Autowired private AlunoService alunoService;

    @Nested
    class buscarTodosAlunos {
        @Test
        @DisplayName("Deve buscar todos os alunos com sucesso")
        void deveBuscarTodosAlunosComSucesso() {
            Aluno aluno = new Aluno(
                1L, 
                "aluno", 
                "email@email.com", 
                "senha", 
                LocalDate.now(), 
                "rg", 
                "localNascimento", 
                "nacionalidade", 
                TipoCurso.MESTRADO, 
                "linkLattes", 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now());

            List<Aluno> listaAlunos = List.of(aluno);

            when(alunoRepository.findAll()).thenReturn(listaAlunos);

            List<Aluno> output = alunoService.buscarTodosAlunos();

            assertNotNull(output);
            assertEquals(listaAlunos.size(), output.size());
        }
    }

    
    @Nested
    class buscarPorNumeroUsp {
        @Test
        @DisplayName("Deve buscar o aluno pelo número USP com sucesso quando existir")
        void deveBuscarAlunoPorNumeroUspComSucessoQuandoOptionalEstaPresente() {
            Aluno aluno = new Aluno(
                1L, 
                "aluno", 
                "email@email.com", 
                "senha", 
                LocalDate.now(), 
                "rg", 
                "localNascimento", 
                "nacionalidade", 
                TipoCurso.MESTRADO, 
                "linkLattes", 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now());

            when(alunoRepository.findById(aluno.getNumeroUsp())).thenReturn(Optional.of(aluno));

            Optional<Aluno> alunoOptional = alunoService.buscarPorNumeroUsp(aluno.getNumeroUsp());

            assertEquals(true, alunoOptional.isPresent());
        }

        @Test
        @DisplayName("Deve buscar o aluno pelo número USP com sucesso quando não existir")
        void deveBuscarAlunoPorNumeroUspComSucessoQuandoOptionalEstaVazio() {
            Long numeroUsp = 1L;

            when(alunoRepository.findById(numeroUsp)).thenReturn(Optional.empty());

            Optional<Aluno> alunoOptional = alunoService.buscarPorNumeroUsp(numeroUsp);

            assertEquals(true, alunoOptional.isEmpty());
        }
    }


    @Nested
    class criarAluno {
        @Test
        @DisplayName("Deve criar um aluno com sucesso")
        void deveCriarUmAlunoComSucesso() {
            Aluno aluno = new Aluno(
                1L, 
                "aluno", 
                "email@email.com", 
                "senha", 
                LocalDate.now(), 
                "rg", 
                "localNascimento", 
                "nacionalidade", 
                TipoCurso.MESTRADO, 
                "linkLattes", 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now());

            AlunoDto alunoDto = new AlunoDto(
                1L, 
                "aluno", 
                "email@email.com", 
                "senha", 
                LocalDate.now(), 
                "rg", 
                "localNascimento", 
                "nacionalidade", 
                TipoCurso.MESTRADO, 
                "linkLattes", 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now());

            when(alunoRepository.existsById(alunoDto.numeroUsp())).thenReturn(false);
            when(passwordEncoder.encode(alunoDto.senha())).thenReturn("senhaCriptografada");
            when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

            Long output = alunoService.criarAluno(alunoDto);

            assertNotNull(output);
            assertEquals(aluno.getNumeroUsp(), output);
            verify(alunoRepository, times(2)).save(any(Aluno.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando número USP já estiver cadastrado")
        void deveLancarExcecaoQuandoNumeroUspDoAlunoJaEstiverCadastrado() {
            AlunoDto alunoDto = new AlunoDto(
                1L, 
                "aluno", 
                "email@email.com", 
                "senha", 
                LocalDate.now(), 
                "rg", 
                "localNascimento", 
                "nacionalidade", 
                TipoCurso.MESTRADO, 
                "linkLattes", 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now());

            when(alunoRepository.existsById(anyLong())).thenReturn(true);

            assertThrows(EntidadeDuplicadaException.class, () -> alunoService.criarAluno(alunoDto));
        }
    }


    @Nested
    class atualizarAluno {
        @Test
        @DisplayName("Deve atualizar um aluno com sucesso")
        void deveAtualizarAlunoComSucesso() {
            Aluno aluno = new Aluno(
                1L, 
                "aluno", 
                "email@email.com", 
                "senha", 
                LocalDate.now(), 
                "rg", 
                "localNascimento", 
                "nacionalidade", 
                TipoCurso.MESTRADO, 
                "linkLattes", 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now());

            AlunoDto alunoAtualizadoDto = new AlunoDto(
                1L, 
                "alunoAtualizado", 
                "emailAtualizado@email.com", 
                "senha", 
                LocalDate.now(), 
                "rgAtualizado", 
                "localNascimentoAtualizado", 
                "nacionalidadeAtualizado", 
                TipoCurso.MESTRADO, 
                "linkLattesAtualizado", 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now());


            when(alunoRepository.findById(aluno.getNumeroUsp())).thenReturn(Optional.of(aluno));
            when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

            Aluno alunoAtualizado = alunoService.atualizarAluno(aluno.getNumeroUsp(), alunoAtualizadoDto);

            assertNotNull(alunoAtualizado);
            assertEquals(alunoAtualizadoDto.numeroUsp(), alunoAtualizado.getNumeroUsp());
            assertEquals(alunoAtualizadoDto.nomeCompleto(), alunoAtualizado.getNomeCompleto());
            assertEquals(alunoAtualizadoDto.email(), alunoAtualizado.getEmail());
            assertEquals(alunoAtualizadoDto.dataNascimento(), alunoAtualizado.getDataNascimento());
            assertEquals(alunoAtualizadoDto.rg(), alunoAtualizado.getRg());
            assertEquals(alunoAtualizadoDto.localNascimento(), alunoAtualizado.getLocalNascimento());
            assertEquals(alunoAtualizadoDto.nacionalidade(), alunoAtualizado.getNacionalidade());
            assertEquals(alunoAtualizadoDto.tipoCurso(), alunoAtualizado.getTipoCurso());
            assertEquals(alunoAtualizadoDto.linkLattes(), alunoAtualizado.getLinkLattes());
            assertEquals(alunoAtualizadoDto.dataMatricula(), alunoAtualizado.getDataMatricula());
            assertEquals(alunoAtualizadoDto.dataAprovacaoExameQualificacao(), alunoAtualizado.getDataAprovacaoExameQualificacao());
            assertEquals(alunoAtualizadoDto.dataAprovacaoExameProficienciaLinguas(), alunoAtualizado.getDataAprovacaoExameProficienciaLinguas());
            assertEquals(alunoAtualizadoDto.dataLimiteDepositoTrabalhoFinal(), alunoAtualizado.getDataLimiteDepositoTrabalhoFinal());

            verify(alunoRepository, times(1)).save(any(Aluno.class));
        }

        @Test
        @DisplayName("Deve retornar null quando aluno não existir")
        void deveRetornarNullQuandoAlunoNaoExistir() {
            AlunoDto alunoDto = new AlunoDto(
                1L, 
                "aluno", 
                "email@email.com", 
                "senha", 
                LocalDate.now(), 
                "rg", 
                "localNascimento", 
                "nacionalidade", 
                TipoCurso.MESTRADO, 
                "linkLattes", 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now(), 
                LocalDate.now());

            when(alunoRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertNull(alunoService.atualizarAluno(alunoDto.numeroUsp(), alunoDto));
        }
    }


    @Nested
    class deletarAluno {
        @Test
        @DisplayName("Deve deletar um aluno com sucesso")
        void deveDeletarAlunoComSucesso() {
            Long numeroUspAluno = 1L;

            when(alunoRepository.existsById(numeroUspAluno)).thenReturn(true);

            boolean alunoDeletado = alunoService.deletarAluno(numeroUspAluno);

            assertEquals(true, alunoDeletado);
            verify(alunoRepository, times(1)).deleteById(numeroUspAluno);
        }

        @Test
        @DisplayName("Deve retornar false quando o aluno não existir")
        void deveRetornarFalseQuandoAlunoNaoExiste() {
            Long numeroUspAluno = 1L;

            when(alunoRepository.existsById(anyLong())).thenReturn(false);

            boolean alunoDeletado = alunoService.deletarAluno(numeroUspAluno);

            assertEquals(false, alunoDeletado);
        }
    }
}

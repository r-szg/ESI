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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.usp.projetoa.dto.DocenteDto;
import br.usp.projetoa.exception.EntidadeDuplicadaException;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DocenteRepository;
import br.usp.projetoa.repository.RoleRepository;

@SpringBootTest(classes = DocenteService.class)
public class DocenteServiceTest {
    @MockBean private DocenteRepository docenteRepository;
    @MockBean private AlunoRepository alunoRepository;
    @MockBean private RoleRepository roleRepository;
    @MockBean private PasswordEncoder passwordEncoder;

    @Autowired private DocenteService docenteService;

    @Nested
    class buscarTodosDocentes {
        @Test
        @DisplayName("Deve buscar todos os docentes com sucesso")
        void deveBuscarTodosDocentesComSucesso() {
            Docente docente = new Docente(1L, "nomeCompleto", "senha", "email@email.com", true, false);

            List<Docente> listaDocentes = List.of(docente);

            when(docenteRepository.findAll()).thenReturn(listaDocentes);

            List<Docente> output = docenteService.buscarTodosDocentes();

            assertNotNull(output);
            assertEquals(listaDocentes.size(), output.size());
        }
    }


    @Nested
    class buscarPorNumeroUsp {
        @Test
        @DisplayName("Deve buscar o docente pelo número USP com sucesso quando existir")
        void deveBuscarDocentePorNumeroUspComSucessoQuandoOptionalEstaPresente() {
            Docente docente = new Docente(1L, "nomeCompleto", "senha", "email@email.com", true, false);

            when(docenteRepository.findById(docente.getNumeroUsp())).thenReturn(Optional.of(docente));

            Optional<Docente> docenteOptional = docenteService.buscarPorNumeroUsp(docente.getNumeroUsp());

            assertEquals(true, docenteOptional.isPresent());
        }

        @Test
        @DisplayName("Deve buscar o docente pelo número USP com sucesso quando não existir")
        void deveBuscarDocentePorNumeroUspComSucessoQuandoOptionalEstaVazio() {
            Long numeroUsp = 1L;

            when(docenteRepository.findById(numeroUsp)).thenReturn(Optional.empty());

            Optional<Docente> docenteOptional = docenteService.buscarPorNumeroUsp(numeroUsp);

            assertEquals(true, docenteOptional.isEmpty());
        }
    }


    @Nested
    class criarDocente {
        @Test
        @DisplayName("Deve criar docente com sucesso")
        void deveCriarDocenteComSucesso() {
            Docente docente = new Docente(1L, "nomeCompleto", "senha", "email@email.com", true, false);

            DocenteDto docenteDto = new DocenteDto(1L, "nomeCompleto", "senha", "email@email.com", true, false);

            when(docenteRepository.existsById(docenteDto.numeroUsp())).thenReturn(false);
            when(passwordEncoder.encode(docenteDto.senha())).thenReturn("senhaCriptografada");
            when(docenteRepository.save(any(Docente.class))).thenReturn(docente);

            Long output = docenteService.criarDocente(docenteDto);

            assertNotNull(output);
            assertEquals(docente.getNumeroUsp(), output);
            verify(docenteRepository, times(1)).save(any(Docente.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando número USP já estiver cadastrado")
        void deveLancarExcecaoQuandoNumeroUspDoDocenteJaEstiverCadastrado() {
            DocenteDto docenteDto = new DocenteDto(1L, "nomeCompleto", "senha", "email@email.com", true, false);

            when(docenteRepository.existsById(anyLong())).thenReturn(true);

            assertThrows(EntidadeDuplicadaException.class, () -> docenteService.criarDocente(docenteDto));
        }
    }


    @Nested
    class atualizarDocente {
        @Test
        @DisplayName("Deve atualizar um docente com sucesso")
        void deveAtualizarDocenteComSucesso() {
            Docente docente = new Docente(1L, "nomeCompleto", "senha", "email@email.com", true, false);

            DocenteDto docenteAtualizadoDto = new DocenteDto(1L, 
                "nomeCompletoAtualizado", 
                "senha", 
                "emailAtualizado@email.com", 
                true, 
                false);

            when(docenteRepository.findById(docente.getNumeroUsp())).thenReturn(Optional.of(docente));
            when(docenteRepository.save(any(Docente.class))).thenReturn(docente);

            Docente docenteAtualizado = docenteService.atualizarDocente(docente.getNumeroUsp(), docenteAtualizadoDto);

            assertNotNull(docenteAtualizado);
            assertEquals(docenteAtualizadoDto.nomeCompleto(), docenteAtualizado.getNomeCompleto());
            assertEquals(docenteAtualizadoDto.email(), docenteAtualizado.getEmail());
        }

        @Test
        @DisplayName("Deve retornar null quando docente não existir")
        void deveRetornarNullQuandoDocenteNaoExistir() {
            DocenteDto docenteDto = new DocenteDto(1L, "nomeCompleto", "senha", "email@email.com", true, false);

            when(docenteRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertNull(docenteService.atualizarDocente(docenteDto.numeroUsp(), docenteDto));
        }
    }


    @Nested
    class deletarDocente {
        @Test
        @DisplayName("Deve deletar um docente com sucesso")
        void deveDeletarDocenteComSucesso() {
            Long numeroUspDocente = 1L;

            when(docenteRepository.existsById(numeroUspDocente)).thenReturn(true);

            boolean docenteDeletado = docenteService.deletarDocente(numeroUspDocente);

            assertEquals(true, docenteDeletado);
            verify(docenteRepository, times(1)).deleteById(numeroUspDocente);
        }

        @Test
        @DisplayName("Deve retornar false quando o docente não existir")
        void deveRetornarFalseQuandoDocenteNaoExistir() {
            Long numeroUspDocente = 1L;

            when(docenteRepository.existsById(anyLong())).thenReturn(false);

            boolean docenteDeletado = docenteService.deletarDocente(numeroUspDocente);

            assertEquals(false, docenteDeletado);
        }
    }
}

package br.usp.projetoa.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.usp.projetoa.dto.AlunoDto;
import br.usp.projetoa.enums.RoleEnum;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.security.AuthenticatedUserUtil;
import br.usp.projetoa.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/alunos")
@Tag(name = "Aluno Controller", description = "Operações relacionadas aos alunos")
public class AlunoController {
    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @Operation(summary = "Obter todos os alunos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alunos recuperados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Aluno>> buscarTodosAlunos() {
        List<Aluno> alunos = alunoService.buscarTodosAlunos();

        return ResponseEntity.ok(alunos);
    } 


    @Operation(summary = "Obter aluno pelo número USP")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno recuperado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    @GetMapping("/{numeroUsp}")
    public ResponseEntity<Aluno> buscarAlunoPorNumeroUsp(@PathVariable("numeroUsp") Long numeroUsp) {
        if (AuthenticatedUserUtil.getRolesLogado().contains(RoleEnum.ROLE_ALUNO.toString())
                && !AuthenticatedUserUtil.temPermissaoParaAcessarNumeroUsp(numeroUsp)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Aluno> alunoOptional = alunoService.buscarPorNumeroUsp(numeroUsp);

        if (alunoOptional.isPresent()) return ResponseEntity.ok(alunoOptional.get());

        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Criar um aluno")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Aluno criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
        @ApiResponse(responseCode = "409", description = "Número USP do aluno já está cadastrado")
    }) 
    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Aluno> criarAluno(@RequestBody @Valid AlunoDto alunoDto, BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        Long numeroUsp = alunoService.criarAluno(alunoDto);

        return ResponseEntity.created(URI.create("/api/alunos/" + numeroUsp.toString())).build();
    }


    @Operation(summary = "Atualizar um aluno")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    }) 
    @PutMapping("/{numeroUsp}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable("numeroUsp") Long numeroUsp, 
                                                @RequestBody @Valid AlunoDto alunoDto, 
                                                BindingResult result) {
        if (AuthenticatedUserUtil.getRolesLogado().contains(RoleEnum.ROLE_ALUNO.toString())
                && !AuthenticatedUserUtil.temPermissaoParaAcessarNumeroUsp(numeroUsp)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        Aluno alunoAtualizado = alunoService.atualizarAluno(numeroUsp, alunoDto);

        if (alunoAtualizado == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(alunoAtualizado);
    }


    @Operation(summary = "Deletar um aluno")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Aluno deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    }) 
    @DeleteMapping("/{numeroUsp}")
    public ResponseEntity<Void> deletarAluno(@PathVariable("numeroUsp") Long numeroUsp) {
        boolean alunoDeletado = alunoService.deletarAluno(numeroUsp);

        if (alunoDeletado) return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }
}

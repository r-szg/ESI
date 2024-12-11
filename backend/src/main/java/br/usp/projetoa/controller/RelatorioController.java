package br.usp.projetoa.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.usp.projetoa.dto.RelatorioDto;
import br.usp.projetoa.enums.RoleEnum;
import br.usp.projetoa.model.Relatorio;
import br.usp.projetoa.security.AuthenticatedUserUtil;
import br.usp.projetoa.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/relatorios")
@Tag(name = "Relatório Controller", description = "Operações relacionadas aos relatórios")
public class RelatorioController {
    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @Operation(summary = "Obter todos os relatórios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatórios recuperados com sucesso"),
    })
    @GetMapping
    public ResponseEntity<List<Relatorio>> buscarTodosRelatorios() {
        List<Relatorio> relatorios = relatorioService.buscarTodosRelatorios();

        return ResponseEntity.ok(relatorios);
    }


    @Operation(summary = "Obter todos os relatórios de um aluno")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatórios do aluno recuperados com sucesso"),
    })
    @GetMapping("/aluno/{numeroUsp}")
    public ResponseEntity<List<Relatorio>> buscarRelatoriosPorAluno(@PathVariable("numeroUsp") Long numeroUsp) {
        if (AuthenticatedUserUtil.getRolesLogado().contains(RoleEnum.ROLE_ALUNO.toString())
                && !AuthenticatedUserUtil.temPermissaoParaAcessarNumeroUsp(numeroUsp)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Relatorio> relatoriosAluno = relatorioService.buscarRelatoriosPorAluno(numeroUsp);

        return ResponseEntity.ok(relatoriosAluno);
    }


    @Operation(summary = "Obter todos os relatórios de um orientador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatórios do orientador recuperados com sucesso"),
    })
    @GetMapping("/orientador/{numeroUsp}")
    public ResponseEntity<List<Relatorio>> buscarRelatoriosPorOrientador(@PathVariable("numeroUsp") Long numeroUsp) {
        List<Relatorio> relatoriosOrientador = relatorioService.buscarRelatoriosPorOrientador(numeroUsp);

        return ResponseEntity.ok(relatoriosOrientador);
    }


    @Operation(summary = "Criar um relatório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Relatório criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
    }) 
    @PostMapping
    public ResponseEntity<Relatorio> criarRelatorio(@RequestBody @Valid RelatorioDto relatorioDto, BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        Long id = relatorioService.criarRelatorio(relatorioDto);

        return ResponseEntity.created(URI.create("/api/relatorios/" + id.toString())).build();
    }


    @Operation(summary = "Fazer o reenvio de um relatório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatório reenviado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
        @ApiResponse(responseCode = "404", description = "Relatório não encontrado")
    }) 
    @PutMapping("/{numeroUspAluno}/{relatorioId}")
    public ResponseEntity<Relatorio> reenviarRelatorio(@PathVariable("numeroUspAluno") Long numeroUspAluno, 
                                                       @PathVariable("relatorioId") Long relatorioId, 
                                                       @RequestBody @Valid RelatorioDto relatorioDto, 
                                                       BindingResult result) {
        if (AuthenticatedUserUtil.getRolesLogado().contains(RoleEnum.ROLE_ALUNO.toString())
                && !AuthenticatedUserUtil.temPermissaoParaAcessarNumeroUsp(numeroUspAluno)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        Relatorio relatorioReenviado = relatorioService.reenviarRelatorio(numeroUspAluno, relatorioId, relatorioDto);

        if (relatorioReenviado == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(relatorioReenviado);
    }
}

package br.usp.projetoa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.usp.projetoa.enums.RoleEnum;
import br.usp.projetoa.model.StatusEntregaRelatorio;
import br.usp.projetoa.security.AuthenticatedUserUtil;
import br.usp.projetoa.service.StatusEntregaRelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/status_relatorios")
@Tag(name = "Controller do Status da Entrega do Relatório", description = "Operações relacionadas aos status das entregas dos relatórios")
public class StatusEntregaRelatorioController {
    private final StatusEntregaRelatorioService statusEntregaRelatorioService;

    public StatusEntregaRelatorioController(StatusEntregaRelatorioService statusEntregaRelatorioService) {
        this.statusEntregaRelatorioService = statusEntregaRelatorioService;
    }

    @Operation(summary = "Obter status da entrega do relatório do semestre atual de um determinado aluno")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status da entrega do relatório recuperado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Status da entrega do relatório não encontrado"),
    })
    @GetMapping("/{numeroUspAluno}")
    public ResponseEntity<StatusEntregaRelatorio> buscarStatusPorAlunoNoSemestreAtual(@PathVariable("numeroUspAluno") Long numeroUspAluno) {
        if (AuthenticatedUserUtil.getRolesLogado().contains(RoleEnum.ROLE_ALUNO.toString())
                && !AuthenticatedUserUtil.temPermissaoParaAcessarNumeroUsp(numeroUspAluno)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        StatusEntregaRelatorio statusEntregaRelatorio = statusEntregaRelatorioService.buscarStatusPorAlunoNoSemestreAtual(numeroUspAluno);

        if (statusEntregaRelatorio == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(statusEntregaRelatorio);
    }
}

package br.usp.projetoa.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.usp.projetoa.dto.MatriculaDto;
import br.usp.projetoa.model.Matricula;
import br.usp.projetoa.service.MatriculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/matriculas")
@Tag(name = "Matricula Controller", description = "Operações relacionadas às matriculas")
public class MatriculaController {
    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @Operation(summary = "Obter todas as matriculas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matrículas recuperadas com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Matricula>> buscarTodasMatriculas() {
        return ResponseEntity.ok(matriculaService.buscarTodasMatriculas());
    }


    @Operation(summary = "Obter matrícula(s) pelo número USP de um aluno")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matrícula(s) recuperada(s) com sucesso"),
    })
    @GetMapping("/aluno/{numeroUsp}")
    public ResponseEntity<List<Matricula>> buscarMatriculasPorAluno(@PathVariable Long numeroUsp) {
        List<Matricula> matriculas = matriculaService.buscarMatriculasPorAluno(numeroUsp);

        return ResponseEntity.ok(matriculas);
    }


    @Operation(summary = "Matricular um aluno")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Aluno matriculado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
    }) 
    @PostMapping
    public ResponseEntity<Matricula> matricularAluno(@RequestBody @Valid MatriculaDto matriculaDto, BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        Matricula novaMatricula = matriculaService.matricularAluno(matriculaDto);

        return ResponseEntity.created(URI.create("/api/matriculas/aluno/" + novaMatricula.getAluno().getNumeroUsp())).build();
    }
}

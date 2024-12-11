package br.usp.projetoa.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.usp.projetoa.dto.DisciplinaDto;
import br.usp.projetoa.model.Disciplina;
import br.usp.projetoa.service.DisciplinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/disciplinas")
@Tag(name = "Disciplina Controller", description = "Operações relacionadas às disciplinas")
public class DisciplinaController {
    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @Operation(summary = "Obter todas as disciplinas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplinas recuperadas com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Disciplina>> buscarTodasDisciplinas() {
        List<Disciplina> disciplinas = disciplinaService.buscarTodasDisciplinas();

        return ResponseEntity.ok(disciplinas);
    }


    @Operation(summary = "Obter disciplina pelo código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina recuperada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    @GetMapping("/{codigoDisciplina}")
    public ResponseEntity<Disciplina> buscarDisciplinaPorCodigo(@PathVariable("codigoDisciplina") String codigoDisciplina) {
        Optional<Disciplina> disciplinaOptional = disciplinaService.buscarDisciplina(codigoDisciplina);

        if (disciplinaOptional.isPresent()) return ResponseEntity.ok(disciplinaOptional.get());

        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Criar uma disciplina")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Disciplina criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
        @ApiResponse(responseCode = "409", description = "Código da discplina já está cadastrado")
    }) 
    @PostMapping
    public ResponseEntity<Disciplina> criarDisciplina(@RequestBody @Valid DisciplinaDto disciplinaDto, BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        String codigoDisciplina = disciplinaService.criarDisciplina(disciplinaDto);

        return ResponseEntity.created(URI.create("/api/disciplinas/" + codigoDisciplina)).build();
    }


    @Operation(summary = "Atualizar uma disciplina")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    }) 
    @PutMapping("/{codigoDisciplina}")
    public ResponseEntity<Disciplina> atualizarDisciplina(
            @PathVariable("codigoDisciplina") String codigoDisciplina,
            @RequestBody @Valid DisciplinaDto disciplinaDto,
            BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        Disciplina disciplinaAtualizada = disciplinaService.atualizarDisciplina(codigoDisciplina, disciplinaDto);

        if (disciplinaAtualizada == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(disciplinaAtualizada);
    }


    @Operation(summary = "Deletar uma disciplina")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Disciplina deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    }) 
    @DeleteMapping("/{codigoDisciplina}")
    public ResponseEntity<Void> deletarDisciplina(@PathVariable("codigoDisciplina") String codigoDisciplina) {
        boolean disciplinaDeletada = disciplinaService.deletarDisciplina(codigoDisciplina);

        if (disciplinaDeletada) return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }
}

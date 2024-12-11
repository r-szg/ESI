package br.usp.projetoa.controller;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.usp.projetoa.dto.ParecerDto;
import br.usp.projetoa.model.Parecer;
import br.usp.projetoa.service.ParecerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pareceres")
@Tag(name = "Parecer Controller", description = "Operações relacionadas aos pareceres")
public class ParecerController {
    private final ParecerService parecerService;

    public ParecerController(ParecerService parecerService) {
        this.parecerService = parecerService;
    }

    @Operation(summary = "Criar um parecer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Parecer criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
    }) 
    @PostMapping
    public ResponseEntity<Parecer> criarParecer(@RequestBody @Valid ParecerDto parecerDto, BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        Long id = parecerService.criarParecer(parecerDto);

        if (id == null) return ResponseEntity.notFound().build();

        return ResponseEntity.created(URI.create("/api/pareceres/" + id.toString())).build();
    }


    @Operation(summary = "Atualizar um parecer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parecer atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
        @ApiResponse(responseCode = "404", description = "Parecer não encontrado")
    }) 
    @PutMapping("/{parecerId}")
    public ResponseEntity<Parecer> atualizarParecer(@PathVariable("parecerId") Long parecerId,
                                                    @RequestBody @Valid ParecerDto parecerDto,
                                                    BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        Parecer parecerAtualizado = parecerService.atualizarParecer(parecerId, parecerDto);

        if (parecerAtualizado == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(parecerAtualizado);
    }


    @Operation(summary = "Deletar um parecer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Parecer deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Parecer não encontrado")
    }) 
    @DeleteMapping("/{parecerId}")
    public ResponseEntity<Void> deletarParecer(@PathVariable("parecerId") Long parecerId) {
        boolean parecerDeletado = parecerService.deletarParecer(parecerId);

        if (parecerDeletado) return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }
}

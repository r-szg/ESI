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

import br.usp.projetoa.dto.DocenteDto;
import br.usp.projetoa.enums.RoleEnum;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.security.AuthenticatedUserUtil;
import br.usp.projetoa.service.DocenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/docentes")
@Tag(name = "Docente Controller", description = "Operações relacionadas aos docentes")
public class DocenteController {
    private final DocenteService docenteService;

    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @Operation(summary = "Obter todos os docentes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Docentes recuperados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Docente>> buscarTodosDocentes() {
        List<Docente> docentes = docenteService.buscarTodosDocentes();

        return ResponseEntity.ok(docentes);
    } 


    @Operation(summary = "Obter docente pelo número USP")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Docente recuperado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Docente não encontrado")
    })
    @GetMapping("/{numeroUsp}")
    public ResponseEntity<Docente> buscarDocentePorNumeroUsp(@PathVariable("numeroUsp") Long numeroUsp) {
        if (AuthenticatedUserUtil.getRolesLogado().contains(RoleEnum.ROLE_ORIENTADOR.toString())
                && !AuthenticatedUserUtil.temPermissaoParaAcessarNumeroUsp(numeroUsp)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Docente> docenteOptional = docenteService.buscarPorNumeroUsp(numeroUsp);

        if (docenteOptional.isPresent()) return ResponseEntity.ok(docenteOptional.get());

        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Criar um docente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Docente criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
        @ApiResponse(responseCode = "409", description = "Número USP do docente já está cadastrado")
    }) 
    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Docente> criarDocente(@RequestBody @Valid DocenteDto docenteDto, BindingResult result) {
        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        Long numeroUsp = docenteService.criarDocente(docenteDto);

        return ResponseEntity.created(URI.create("/api/docentes/" + numeroUsp.toString())).build();
    }


    @Operation(summary = "Atualizar um docente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Docente atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Entrada fornecida inválida"),
        @ApiResponse(responseCode = "404", description = "Docente não encontrado")
    }) 
    @PutMapping("/{numeroUsp}")
    public ResponseEntity<Docente> atualizarDocente(@PathVariable("numeroUsp") Long numeroUsp, 
                                                @RequestBody @Valid DocenteDto docenteDto, 
                                                BindingResult result) {
        if (AuthenticatedUserUtil.getRolesLogado().contains(RoleEnum.ROLE_ORIENTADOR.toString())
                && !AuthenticatedUserUtil.temPermissaoParaAcessarNumeroUsp(numeroUsp)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (result.hasErrors()) return ResponseEntity.badRequest().build();

        Docente docenteAtualizado = docenteService.atualizarDocente(numeroUsp, docenteDto);

        if (docenteAtualizado == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(docenteAtualizado);
    }

    @Operation(summary = "Deletar um docente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Docente deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Docente não encontrado")
    }) 
    @DeleteMapping("/{numeroUsp}")
    public ResponseEntity<Void> deletarDocente(@PathVariable("numeroUsp") Long numeroUsp) {
        if (AuthenticatedUserUtil.getRolesLogado().contains(RoleEnum.ROLE_ORIENTADOR.toString())
                && !AuthenticatedUserUtil.temPermissaoParaAcessarNumeroUsp(numeroUsp)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean docenteDeletado = docenteService.deletarDocente(numeroUsp);

        if (docenteDeletado) return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }
}

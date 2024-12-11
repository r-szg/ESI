package br.usp.projetoa.dto;

import jakarta.validation.constraints.NotBlank;

public record DisciplinaDto (

    @NotBlank
    String codigoDisciplina,

    @NotBlank
    String nomeDisciplina
) {

}

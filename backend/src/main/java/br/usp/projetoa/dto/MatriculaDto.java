package br.usp.projetoa.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import br.usp.projetoa.enums.SituacaoDisciplina;

public record MatriculaDto (
    @NotNull
    @Min(0)
    Long numeroUsp,

    @NotBlank
    String codigoDisciplina,

    @NotNull
    SituacaoDisciplina situacaoDisciplina
) {

}

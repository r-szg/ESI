package br.usp.projetoa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import br.usp.projetoa.enums.SituacaoRelatorio;

public record ParecerDto(
    @NotNull
    Long relatorioId,
    @NotBlank
    String parecer,
    @NotNull
    SituacaoRelatorio desempenhoRelatorio
) {
}

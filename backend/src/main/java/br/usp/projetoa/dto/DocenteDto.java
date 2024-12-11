package br.usp.projetoa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DocenteDto(

    @NotNull
    Long numeroUsp,

    @NotBlank
    String nomeCompleto,

    @NotBlank
    String senha,

    @NotBlank
    @Email
    String email,

    @NotNull
    Boolean ehOrientador,

    @NotNull
    Boolean ehMembroCCP
) {
}

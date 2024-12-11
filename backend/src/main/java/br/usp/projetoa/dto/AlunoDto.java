package br.usp.projetoa.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import br.usp.projetoa.enums.TipoCurso;

public record AlunoDto (
    @NotNull
    Long numeroUsp,

    @NotBlank
    String nomeCompleto,

    @NotBlank
    @Email
    String email,

    @NotBlank
    String senha,

    @NotNull
    LocalDate dataNascimento,

    @NotBlank
    String rg,

    @NotBlank
    String localNascimento,

    @NotBlank
    String nacionalidade,

    @NotNull
    TipoCurso tipoCurso,

    @NotNull
    String linkLattes,

    @NotNull
    LocalDate dataMatricula,

    @NotNull
    LocalDate dataAprovacaoExameQualificacao,

    @NotNull
    LocalDate dataAprovacaoExameProficienciaLinguas,

    @NotNull
    LocalDate dataLimiteDepositoTrabalhoFinal
) {

}

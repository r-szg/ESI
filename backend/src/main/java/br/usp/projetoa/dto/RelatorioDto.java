package br.usp.projetoa.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import br.usp.projetoa.enums.Quantidade;
import br.usp.projetoa.enums.SituacaoExame;
import br.usp.projetoa.enums.SituacaoRelatorio;
import br.usp.projetoa.enums.TipoCurso;

public record RelatorioDto(
    @NotNull
    Long numeroUspParecista,
    
    @NotNull
    LocalDate dataUltimaAtualizacaoLattes,

    @NotNull
    TipoCurso tipoCurso,

    @NotNull
    LocalDate mesAnoIngressoComoAlunoRegular,
    
    @NotNull
    SituacaoRelatorio resultadoUltimoRelatorio,
    
    @NotNull
    Quantidade quantidadeAprovacoesDesdeInicioCurso,

    @NotNull
    Quantidade quantidadeReprovacoes2023S2,
    
    @NotNull
    Quantidade quantidadeReprovacoesDesdeInicioCurso,
    
    @NotNull
    SituacaoExame realizouExameProficienciaLinguas,

    @NotNull
    SituacaoExame realizouExameQualificacao,

    LocalDate prazoMaximoInscricaoExameQualificacao,
    
    @NotNull
    LocalDate prazoMaximoDepositoDissertacao,

    @NotNull
    Quantidade artigosEmFaseEscrita,

    @NotNull
    Quantidade artigosSubmetidosEEmPeriodoAvaliacao,

    @NotNull
    Quantidade artigosAceitosOuPublicados,

    @NotBlank
    String participacaoAtividadesAcademicas2024S1,

    @NotBlank
    String resumoAtividadesPesquisa,

    String declaracaoAdicionalParaCCP,

    Boolean necessidadeApoioCoordenacao
) {
}

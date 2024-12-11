package br.usp.projetoa.enums;


public enum SituacaoExame {
    APROVADO("Sim. Fui aprovado."),
    REPROVADO("Sim. Fui reprovado"),
    NAO_REALIZADO("Não.");

    private final String descricao;

    SituacaoExame(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

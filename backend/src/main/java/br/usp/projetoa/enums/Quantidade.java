package br.usp.projetoa.enums;


public enum Quantidade {
    ZERO(0),
    UM(1),
    DOIS(2),
    TRES(3),
    QUATRO_OU_MAIS(4);

    private final int valor;

    Quantidade (int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}

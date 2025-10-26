package br.com.custo.enums;

public enum TipoCusto {
    ENERGIA("Custo de Energia"),
    FRETE("Custo de Frete"),
    AGUA("Custo de Água");

    private final String descricao;

    TipoCusto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
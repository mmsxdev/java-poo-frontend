package br.com.estoque.enums;

public enum TipoEstoque {
    GASOLINA_COMUM("Gasolina Comum"),
    GASOLINA_ADITIVADA("Gasolina Aditivada"),
    ETANOL("Etanol"),
    DIESEL_S10("Diesel S-10");

    private final String descricao;

    TipoEstoque(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
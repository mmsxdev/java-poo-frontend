package br.com.preco.enums;

/**
 * Representa os tipos de combustível/estoque.
 * OBS: Estes são valores de exemplo. Ajuste conforme os valores exatos do seu backend.
 */
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
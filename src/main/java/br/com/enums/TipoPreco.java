package br.com.enums;

public enum TipoPreco {
    UNITARIO("Preço Unitário"),
    TOTAL("Preço Total");

    private final String descricao;

    private TipoPreco(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

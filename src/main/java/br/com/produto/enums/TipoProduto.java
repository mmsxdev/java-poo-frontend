package br.com.produto.enums;

public enum TipoProduto {
    LUBRIFICANTE("Lubrificantes"),
    ADITIVO("Aditivos"),
    CONVENIENCIA("Conveniência"),
    AUTOMOTIVO("Peças e Acessórios Automotivos");

    private final String descricao;

    TipoProduto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
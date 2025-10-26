package br.com.produto.model;

import br.com.produto.enums.TipoProduto;
import com.google.gson.annotations.SerializedName;

/**
 * Modelo (ou DTO) para representar os dados de Produto na camada de visão (Swing).
 */
public class ProdutoModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("referencia")
    private String referencia;

    @SerializedName("fornecedor")
    private String fornecedor;

    @SerializedName("marca")
    private String marca;

    @SerializedName("categoria")
    private TipoProduto categoria;

    // Construtor padrão
    public ProdutoModel() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getFornecedor() { return fornecedor; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public TipoProduto getCategoria() { return categoria; }
    public void setCategoria(TipoProduto categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return nome;
    }
}
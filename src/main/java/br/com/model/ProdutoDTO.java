package br.com.model;

import br.com.enums.TipoProduto;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object (DTO) para a entidade Produto.
 */
public class ProdutoDTO {

    private Long id;
    private String nome;
    private String referencia;
    private String fornecedor;
    private String marca;

    // Diz ao Jackson para mapear o campo "categoria" do JSON para este campo "tipoProduto"
    @JsonProperty("categoria")
    private TipoProduto tipoProduto;

    // Construtor vazio para o Jackson
    public ProdutoDTO() {
    }

    public ProdutoDTO(Long id, String nome, String referencia, String fornecedor, String marca, TipoProduto tipoProduto) {
        this.id = id;
        this.nome = nome;
        this.referencia = referencia;
        this.fornecedor = fornecedor;
        this.marca = marca;
        this.tipoProduto = tipoProduto;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }
}
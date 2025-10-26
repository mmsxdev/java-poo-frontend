package br.com.estoque.model;

import br.com.estoque.enums.TipoEstoque;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo (ou DTO) para representar os dados de Estoque na camada de visão (Swing).
 */
public class EstoqueModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("tipoEstoque")
    private TipoEstoque tipoEstoque;

    @SerializedName("quantidade")
    private BigDecimal quantidade;

    @SerializedName("localTanque")
    private String localTanque;

    @SerializedName("localEndereco")
    private String localEndereco;

    @SerializedName("loteFabricacao")
    private String loteFabricacao;

    @SerializedName("dataValidade")
    private LocalDate dataValidade;

    // Construtor padrão
    public EstoqueModel() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoEstoque getTipoEstoque() { return tipoEstoque; }
    public void setTipoEstoque(TipoEstoque tipoEstoque) { this.tipoEstoque = tipoEstoque; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

    public String getLocalTanque() { return localTanque; }
    public void setLocalTanque(String localTanque) { this.localTanque = localTanque; }

    public String getLocalEndereco() { return localEndereco; }
    public void setLocalEndereco(String localEndereco) { this.localEndereco = localEndereco; }

    public String getLoteFabricacao() { return loteFabricacao; }
    public void setLoteFabricacao(String loteFabricacao) { this.loteFabricacao = loteFabricacao; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    @Override
    public String toString() {
        return "Estoque de " + (tipoEstoque != null ? tipoEstoque.getDescricao() : "N/A");
    }
}
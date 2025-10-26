package br.com.preco.model;

import br.com.preco.enums.TipoEstoque;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo (ou DTO) para representar os dados de Preco na camada de visão (Swing).
 */
public class PrecoModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("tipoEstoque")
    private TipoEstoque tipoEstoque;

    @SerializedName("valor")
    private BigDecimal valor;

    @SerializedName("dataVigencia")
    private LocalDate dataVigencia;

    // Construtor padrão
    public PrecoModel() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoEstoque getTipoEstoque() { return tipoEstoque; }
    public void setTipoEstoque(TipoEstoque tipoEstoque) { this.tipoEstoque = tipoEstoque; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDate getDataVigencia() { return dataVigencia; }
    public void setDataVigencia(LocalDate dataVigencia) { this.dataVigencia = dataVigencia; }

    @Override
    public String toString() {
        return "Preço: " + valor + " (" + tipoEstoque + ")";
    }
}
package br.com.custo.model;

import br.com.custo.enums.TipoCusto;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo (ou DTO) para representar os dados de Custo na camada de visão (Swing).
 */
public class CustoModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("tipoCusto")
    private TipoCusto tipoCusto;

    @SerializedName("imposto")
    private BigDecimal imposto;

    @SerializedName("custoVariavel")
    private BigDecimal custoVariavel;

    @SerializedName("custoFixo")
    private BigDecimal custoFixo;

    @SerializedName("margemLucro")
    private BigDecimal margemLucro;

    @SerializedName("dataProcessamento")
    private LocalDate dataProcessamento;

    // Construtor padrão
    public CustoModel() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoCusto getTipoCusto() { return tipoCusto; }
    public void setTipoCusto(TipoCusto tipoCusto) { this.tipoCusto = tipoCusto; }

    public BigDecimal getImposto() { return imposto; }
    public void setImposto(BigDecimal imposto) { this.imposto = imposto; }

    public BigDecimal getCustoVariavel() { return custoVariavel; }
    public void setCustoVariavel(BigDecimal custoVariavel) { this.custoVariavel = custoVariavel; }

    public BigDecimal getCustoFixo() { return custoFixo; }
    public void setCustoFixo(BigDecimal custoFixo) { this.custoFixo = custoFixo; }

    public BigDecimal getMargemLucro() { return margemLucro; }
    public void setMargemLucro(BigDecimal margemLucro) { this.margemLucro = margemLucro; }

    public LocalDate getDataProcessamento() { return dataProcessamento; }
    public void setDataProcessamento(LocalDate dataProcessamento) { this.dataProcessamento = dataProcessamento; }

    @Override
    public String toString() {
        return "Custo de " + (tipoCusto != null ? tipoCusto.getDescricao() : "N/A");
    }
}
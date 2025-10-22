package br.com.model;
import br.com.enums.TipoEstoque;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) para a entidade Estoque.
 * Esta classe é usada para transferir dados de estoque entre a API e a aplicação Swing.
 */
public class EstoqueDTO {

    private Long id;
    private TipoEstoque tipoEstoque;
    private BigDecimal quantidade;
    private String localTanque;
    private String localEndereco;
    private String loteFabricacao;
    private LocalDate dataValidade;

    // Construtor vazio é importante para bibliotecas de serialização/deserialização
    public EstoqueDTO() {
    }

    // Construtor completo para facilitar a criação de objetos
    public EstoqueDTO(Long id, TipoEstoque tipoEstoque, BigDecimal quantidade, String localTanque, String localEndereco, String loteFabricacao, LocalDate dataValidade) {
        this.id = id;
        this.tipoEstoque = tipoEstoque;
        this.quantidade = quantidade;
        this.localTanque = localTanque;
        this.localEndereco = localEndereco;
        this.loteFabricacao = loteFabricacao;
        this.dataValidade = dataValidade;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoEstoque getTipoEstoque() {
        return tipoEstoque;
    }

    public void setTipoEstoque(TipoEstoque tipoEstoque) {
        this.tipoEstoque = tipoEstoque;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public String getLocalTanque() {
        return localTanque;
    }

    public void setLocalTanque(String localTanque) {
        this.localTanque = localTanque;
    }

    public String getLocalEndereco() {
        return localEndereco;
    }

    public void setLocalEndereco(String localEndereco) {
        this.localEndereco = localEndereco;
    }

    public String getLoteFabricacao() {
        return loteFabricacao;
    }

    public void setLoteFabricacao(String loteFabricacao) {
        this.loteFabricacao = loteFabricacao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }
}
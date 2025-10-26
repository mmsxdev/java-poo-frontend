package br.com.pessoa.model;

import br.com.pessoa.enums.TipoPessoa;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

/**
 * Modelo (ou DTO) para representar os dados de Pessoa na camada de visão (Swing).
 * Facilita a transferência de dados entre a API e a interface do usuário.
 */
public class PessoaModel {

    @SerializedName("id")
    private Long id;
    @SerializedName("nomeCompleto")
    private String nomeCompleto;
    @SerializedName("cpfCnpj")
    private String cpfCnpj;
    @SerializedName("numeroCtps")
    private Long numeroCtps;
    @SerializedName("dataNascimento")
    private LocalDate dataNascimento;
    @SerializedName("tipoPessoa")
    private TipoPessoa tipoPessoa;

    // Construtor padrão é útil para bibliotecas de desserialização JSON como Gson/Jackson
    public PessoaModel() {
    }

    public PessoaModel(Long id, String nomeCompleto, String cpfCnpj, Long numeroCtps, LocalDate dataNascimento, TipoPessoa tipoPessoa) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpfCnpj = cpfCnpj;
        this.numeroCtps = numeroCtps;
        this.dataNascimento = dataNascimento;
        this.tipoPessoa = tipoPessoa;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }

    public Long getNumeroCtps() { return numeroCtps; }
    public void setNumeroCtps(Long numeroCtps) { this.numeroCtps = numeroCtps; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public TipoPessoa getTipoPessoa() { return tipoPessoa; }
    public void setTipoPessoa(TipoPessoa tipoPessoa) { this.tipoPessoa = tipoPessoa; }

    @Override
    public String toString() {
        // Útil para exibir em ComboBoxes ou listas
        return nomeCompleto;
    }
}
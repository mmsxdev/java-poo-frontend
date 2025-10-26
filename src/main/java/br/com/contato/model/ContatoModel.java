package br.com.contato.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo (ou DTO) para representar os dados de Contato na camada de visão (Swing).
 */
public class ContatoModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("telefone")
    private String telefone;

    @SerializedName("email")
    private String email;

    @SerializedName("endereco")
    private String endereco;

    // Construtor padrão
    public ContatoModel() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    @Override
    public String toString() {
        return "Contato: " + email;
    }
}
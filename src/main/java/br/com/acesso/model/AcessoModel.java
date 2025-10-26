package br.com.acesso.model;

import br.com.acesso.enums.TipoAcesso;
import com.google.gson.annotations.SerializedName;

/**
 * Modelo (ou DTO) para representar os dados de Acesso na camada de visão (Swing).
 */
public class AcessoModel {

    @SerializedName("id")
    private Long id;

    @SerializedName("usuario")
    private String usuario;

    // Este campo é usado para ENVIAR a senha. Ele não será preenchido ao RECEBER dados.
    @SerializedName("senha")
    private String senha;

    @SerializedName("tipoAcesso")
    private TipoAcesso tipoAcesso;

    // Construtor padrão
    public AcessoModel() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public TipoAcesso getTipoAcesso() { return tipoAcesso; }
    public void setTipoAcesso(TipoAcesso tipoAcesso) { this.tipoAcesso = tipoAcesso; }

    @Override
    public String toString() {
        return "Usuário: " + usuario;
    }
}
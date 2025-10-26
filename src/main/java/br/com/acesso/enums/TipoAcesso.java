package br.com.acesso.enums;

public enum TipoAcesso {

    ADMINISTRADOR("Acesso Administrador"),
    FUNCIONARIO("Acesso Funcionário"),
    GERENCIA("Acesso Gerência");

    private final String descricao;

    TipoAcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
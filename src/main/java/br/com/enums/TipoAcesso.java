package br.com.enums;

public enum TipoAcesso {

    ADMINISTRADOR("Acesso Administrador"),
    FUNCIONARIO("Acesso Funcionário"),
    GERENCIA("Acesso Gerência");

    private  final String descricao;

    // isso é para o usuario poderr visualizar a descrição do tipo e não igual a maquina vê
    private TipoAcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

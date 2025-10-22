package br.com.service;

import br.com.model.PessoaDTO;

import java.util.List;

public interface IPessoaService {
    List<PessoaDTO> listarTodos() throws Exception;
    PessoaDTO salvar(PessoaDTO pessoaDTO) throws Exception;
    PessoaDTO atualizar(Long id, PessoaDTO pessoaDTO) throws Exception;
    void excluir(Long id) throws Exception;
}
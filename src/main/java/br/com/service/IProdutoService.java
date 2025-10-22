package br.com.service;

import br.com.model.ProdutoDTO;

import java.util.List;

public interface IProdutoService {
    List<ProdutoDTO> listarTodos() throws Exception;

    ProdutoDTO salvar(ProdutoDTO produtoDTO) throws Exception;

    ProdutoDTO atualizar(Long id, ProdutoDTO produtoDTO) throws Exception;

    void excluir(Long id) throws Exception;
}
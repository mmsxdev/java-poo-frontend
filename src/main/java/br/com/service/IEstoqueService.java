package br.com.service;

import br.com.model.EstoqueDTO;

import java.util.List;

public interface IEstoqueService {
    List<EstoqueDTO> listarTodos() throws Exception;

    EstoqueDTO buscarPorId(Long id) throws Exception;

    EstoqueDTO salvar(EstoqueDTO estoqueDTO) throws Exception;

    EstoqueDTO atualizar(Long id, EstoqueDTO estoqueDTO) throws Exception;

    void excluir(Long id) throws Exception;
}
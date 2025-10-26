package br.com.common.service;

import java.util.List;

/**
 * Representa a estrutura de uma resposta paginada do Spring Boot.
 * O tipo 'T' será o nosso modelo, como PessoaModel.
 * @param <T> O tipo do conteúdo da página.
 */
public class PageResponse<T> {
    // Apenas o campo 'content' é necessário para extrair a lista.
    public List<T> content;
}
package br.com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * DTO genérico para representar uma resposta paginada do Spring.
 * A anotação @JsonIgnoreProperties(ignoreUnknown = true) é crucial para
 * que o Jackson ignore campos da paginação que não nos interessam (como 'pageable', 'sort', etc.).
 * @param <T> O tipo do conteúdo da página (ex: PessoaDTO, ProdutoDTO).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageDTO<T> {

    private List<T> content;

    // Construtor padrão
    public PageDTO() {
    }

    // Getter
    public List<T> getContent() {
        return content;
    }

    // Setter
    public void setContent(List<T> content) {
        this.content = content;
    }
}
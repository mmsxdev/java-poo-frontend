package br.com.service;

import br.com.model.ProdutoDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ProdutoApiClient implements IProdutoService {

    private static final String BASE_URL = "http://localhost:8080/api/produtos";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    public ProdutoApiClient() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<ProdutoDTO> listarTodos() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha ao buscar produtos. Código: " + response.statusCode() + " | " + response.body());
        }

        return objectMapper.readValue(response.body(), new TypeReference<List<ProdutoDTO>>() {});
    }

    @Override
    public ProdutoDTO salvar(ProdutoDTO produtoDTO) throws Exception {
        produtoDTO.setId(null);
        String requestBody = objectMapper.writeValueAsString(produtoDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) { // 201 = Created
            throw new RuntimeException("Falha ao salvar produto. Código: " + response.statusCode() + " | " + response.body());
        }

        return objectMapper.readValue(response.body(), ProdutoDTO.class);
    }

    @Override
    public ProdutoDTO atualizar(Long id, ProdutoDTO produtoDTO) throws Exception {
        String requestBody = objectMapper.writeValueAsString(produtoDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) { // 200 = OK
            throw new RuntimeException("Falha ao atualizar produto. Código: " + response.statusCode() + " | " + response.body());
        }

        return objectMapper.readValue(response.body(), ProdutoDTO.class);
    }

    @Override
    public void excluir(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() != 204) { // 204 = No Content
            throw new RuntimeException("Falha ao excluir produto. Código: " + response.statusCode());
        }
    }
}
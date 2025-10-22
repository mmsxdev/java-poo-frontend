package br.com.service;

import br.com.model.EstoqueDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Cliente HTTP para interagir com a API de Estoque do backend.
 * Esta classe implementa a interface de serviço e substitui a versão Mock.
 */
public class EstoqueApiClient implements IEstoqueService {

    // Corrigido: Endpoints REST geralmente usam o plural. Verifique se é o caso no seu backend.
    private static final String BASE_URL = "http://localhost:8080/api/estoque";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    public EstoqueApiClient() {
        this.objectMapper = new ObjectMapper();
        // Essencial para que o Jackson consiga converter as datas (LocalDate)
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<EstoqueDTO> listarTodos() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha ao buscar estoque. Código: " + response.statusCode() + " | " + response.body());
        }

        return objectMapper.readValue(response.body(), new TypeReference<List<EstoqueDTO>>() {});
    }

    @Override
    public EstoqueDTO buscarPorId(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha ao buscar estoque por ID. Código: " + response.statusCode() + " | " + response.body());
        }

        return objectMapper.readValue(response.body(), EstoqueDTO.class);
    }

    @Override
    public EstoqueDTO salvar(EstoqueDTO estoqueDTO) throws Exception {
        // Para salvar, não enviamos o ID. A API irá gerá-lo.
        estoqueDTO.setId(null);
        String requestBody = objectMapper.writeValueAsString(estoqueDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) { // 201 = Created
            throw new RuntimeException("Falha ao salvar estoque. Código: " + response.statusCode() + " | " + response.body());
        }

        // A resposta da API contém o objeto salvo, agora com o ID.
        return objectMapper.readValue(response.body(), EstoqueDTO.class);
    }

    @Override
    public EstoqueDTO atualizar(Long id, EstoqueDTO estoqueDTO) throws Exception {
        String requestBody = objectMapper.writeValueAsString(estoqueDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) { // 200 = OK
            throw new RuntimeException("Falha ao atualizar estoque. Código: " + response.statusCode() + " | " + response.body());
        }

        return objectMapper.readValue(response.body(), EstoqueDTO.class);
    }

    @Override
    public void excluir(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() != 204) { // 204 = No Content
            throw new RuntimeException("Falha ao excluir estoque. Código: " + response.statusCode());
        }
    }
}
package br.com.service;

import br.com.model.PessoaDTO;
import br.com.model.PageDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PessoaApiClient implements IPessoaService {

    private static final String BASE_URL = "http://localhost:8080/api/v1/pessoas";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    public PessoaApiClient() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<PessoaDTO> listarTodos() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha ao buscar pessoas. Código: " + response.statusCode() + " | " + response.body());
        }

        // Define o tipo complexo para PageDTO contendo uma lista de PessoaDTO
        TypeReference<PageDTO<PessoaDTO>> typeRef = new TypeReference<>() {};
        PageDTO<PessoaDTO> pageDTO = objectMapper.readValue(response.body(), typeRef);

        return pageDTO.getContent();
    }

    @Override
    public PessoaDTO salvar(PessoaDTO pessoaDTO) throws Exception {
        pessoaDTO.setId(null);
        String requestBody = objectMapper.writeValueAsString(pessoaDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201) { // 201 = Created
            throw new RuntimeException("Falha ao salvar pessoa. Código: " + response.statusCode() + " | " + response.body());
        }

        return objectMapper.readValue(response.body(), PessoaDTO.class);
    }

    @Override
    public PessoaDTO atualizar(Long id, PessoaDTO pessoaDTO) throws Exception {
        String requestBody = objectMapper.writeValueAsString(pessoaDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) { // 200 = OK
            throw new RuntimeException("Falha ao atualizar pessoa. Código: " + response.statusCode() + " | " + response.body());
        }

        return objectMapper.readValue(response.body(), PessoaDTO.class);
    }

    @Override
    public void excluir(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() != 204) { // 204 = No Content
            throw new RuntimeException("Falha ao excluir pessoa. Código: " + response.statusCode());
        }
    }
}
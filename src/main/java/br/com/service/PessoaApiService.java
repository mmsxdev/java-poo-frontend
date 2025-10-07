package br.com.service;

import br.com.model.Pessoa;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class PessoaApiService implements IPessoaService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final String API_URL = "http://localhost:8080/api/v1/pessoas";

    public PessoaApiService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public List<Pessoa> listarPessoas() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "?size=100")) // Pega até 100 pessoas
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode content = root.path("content");
            return objectMapper.convertValue(content, new TypeReference<List<Pessoa>>() {});
        }
        return Collections.emptyList();
    }

    @Override
    public Pessoa criarPessoa(Pessoa pessoa) throws IOException, InterruptedException {
        String jsonBody = objectMapper.writeValueAsString(pessoa);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) { // CREATED
            return objectMapper.readValue(response.body(), Pessoa.class);
        } else {
            throw new IOException("Falha ao criar pessoa: " + response.body());
        }
    }

    @Override
    public Pessoa atualizarPessoa(Long id, Pessoa pessoa) throws IOException, InterruptedException {
        String jsonBody = objectMapper.writeValueAsString(pessoa);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), Pessoa.class);
        } else {
            throw new IOException("Falha ao atualizar pessoa: " + response.body());
        }
    }

    @Override
    public void deletarPessoa(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() != 204) { // NO_CONTENT
            throw new IOException("Falha ao deletar pessoa. Status: " + response.statusCode());
        }
    }
}
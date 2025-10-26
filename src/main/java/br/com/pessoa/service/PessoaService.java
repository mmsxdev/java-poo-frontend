package br.com.pessoa.service;

import br.com.common.service.LocalDateAdapter;
import br.com.common.exception.ApiServiceException;
import br.com.common.service.PageResponse;
import br.com.pessoa.model.PessoaModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class PessoaService {

    private static final String BASE_URL = "http://localhost:8080/api/v1/pessoas";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson;

    public PessoaService() {
        // Configura o Gson para lidar com LocalDate
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public List<PessoaModel> buscarTodasPessoas() throws ApiServiceException, IOException {
        Request request = new Request.Builder().url(BASE_URL).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiServiceException("Falha ao buscar a lista de pessoas.", response.code());
            }
            if (response.body() == null) {
                return Collections.emptyList();
            }

            // 1. Define o tipo para a resposta paginada (PageResponse<PessoaModel>)
            Type pageType = new TypeToken<PageResponse<PessoaModel>>() {}.getType();

            // 2. Desserializa a resposta JSON completa no objeto PageResponse
            PageResponse<PessoaModel> pageResponse = gson.fromJson(response.body().string(), pageType);

            // 3. Retorna apenas a lista de pessoas que está dentro do campo "content"
            return pageResponse.content;
        }
    }

    public PessoaModel buscarPessoaPorId(Long id) throws ApiServiceException, IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + id).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiServiceException("Pessoa com ID " + id + " não encontrada.", response.code());
            }
            if (response.body() == null) {
                return null;
            }
            return gson.fromJson(response.body().string(), PessoaModel.class);
        }
    }

    public PessoaModel salvarPessoa(PessoaModel pessoa) throws ApiServiceException, IOException {
        String json = gson.toJson(pessoa);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request.Builder requestBuilder = new Request.Builder();
        if (pessoa.getId() == null) {
            // Criar (POST)
            requestBuilder.url(BASE_URL).post(body);
        } else {
            // Atualizar (PUT)
            requestBuilder.url(BASE_URL + "/" + pessoa.getId()).put(body);
        }

        try (Response response = client.newCall(requestBuilder.build()).execute()) {
            if (!response.isSuccessful()) {
                // Lê o corpo do erro apenas uma vez para evitar exceções.
                String errorBody = response.body() != null ? response.body().string() : "O servidor não retornou detalhes do erro.";
                throw new ApiServiceException(errorBody, response.code());
            }
            // Garante que o corpo não seja nulo antes de desserializar.
            String responseBody = response.body() != null ? response.body().string() : null;
            return gson.fromJson(responseBody, PessoaModel.class);
        }
    }

    public void deletarPessoa(Long id) throws ApiServiceException, IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + id).delete().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiServiceException("Falha ao deletar pessoa com ID " + id, response.code());
            }
        }
    }
}
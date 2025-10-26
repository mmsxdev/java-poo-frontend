package br.com.produto.service;

import br.com.common.service.LocalDateAdapter;
import br.com.produto.model.ProdutoModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ProdutoService {

    private static final String BASE_URL = "http://localhost:8080/api/produtos";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson;

    public ProdutoService() {
        // Reutilizamos o LocalDateAdapter, embora Produto não tenha data, é uma boa prática mantê-lo no GsonBuilder.
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public List<ProdutoModel> buscarTodosProdutos() throws IOException {
        Request request = new Request.Builder().url(BASE_URL).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao buscar produtos: " + response);
            }
            if (response.body() == null) {
                return Collections.emptyList();
            }

            // Como a API retorna uma lista direta, a desserialização é mais simples.
            Type listType = new TypeToken<List<ProdutoModel>>() {}.getType();
            return gson.fromJson(response.body().string(), listType);
        }
    }

    public ProdutoModel buscarProdutoPorId(Long id) throws IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + id).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao buscar produto com ID " + id + ": " + response);
            }
            if (response.body() == null) {
                return null;
            }
            return gson.fromJson(response.body().string(), ProdutoModel.class);
        }
    }

    public ProdutoModel salvarProduto(ProdutoModel produto) throws IOException {
        String json = gson.toJson(produto);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request.Builder requestBuilder = new Request.Builder();
        if (produto.getId() == null) {
            // Criar (POST)
            requestBuilder.url(BASE_URL).post(body);
        } else {
            // Atualizar (PUT)
            requestBuilder.url(BASE_URL + "/" + produto.getId()).put(body);
        }

        try (Response response = client.newCall(requestBuilder.build()).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Nenhuma informação de erro.";
                throw new IOException("Erro ao salvar produto: " + response.code() + " - " + errorBody);
            }
            String responseBody = response.body() != null ? response.body().string() : null;
            return gson.fromJson(responseBody, ProdutoModel.class);
        }
    }

    public void deletarProduto(Long id) throws IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + id).delete().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao deletar produto com ID " + id + ": " + response);
            }
        }
    }
}
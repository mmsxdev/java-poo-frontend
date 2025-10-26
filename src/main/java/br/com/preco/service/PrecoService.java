package br.com.preco.service;

import br.com.common.service.LocalDateAdapter;
import br.com.preco.model.PrecoModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class PrecoService {

    private static final String BASE_URL = "http://localhost:8080/api/precos";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson;

    public PrecoService() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public List<PrecoModel> buscarTodosPrecos() throws IOException {
        Request request = new Request.Builder().url(BASE_URL).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao buscar preços: " + response);
            }
            if (response.body() == null) {
                return Collections.emptyList();
            }

            Type listType = new TypeToken<List<PrecoModel>>() {}.getType();
            return gson.fromJson(response.body().string(), listType);
        }
    }

    public PrecoModel buscarPrecoPorId(Long id) throws IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + id).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao buscar preço com ID " + id + ": " + response);
            }
            if (response.body() == null) {
                return null;
            }
            return gson.fromJson(response.body().string(), PrecoModel.class);
        }
    }

    public PrecoModel salvarPreco(PrecoModel preco) throws IOException {
        String json = gson.toJson(preco);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request.Builder requestBuilder = new Request.Builder();
        if (preco.getId() == null) {
            // Criar (POST)
            requestBuilder.url(BASE_URL).post(body);
        } else {
            // Atualizar (PUT)
            requestBuilder.url(BASE_URL + "/" + preco.getId()).put(body);
        }

        try (Response response = client.newCall(requestBuilder.build()).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Nenhuma informação de erro.";
                throw new IOException("Erro ao salvar preço: " + response.code() + " - " + errorBody);
            }
            String responseBody = response.body() != null ? response.body().string() : null;
            return gson.fromJson(responseBody, PrecoModel.class);
        }
    }

    public void deletarPreco(Long id) throws IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + id).delete().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao deletar preço com ID " + id + ": " + response);
            }
        }
    }
}
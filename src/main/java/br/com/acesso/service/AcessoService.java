package br.com.acesso.service;

import br.com.acesso.model.AcessoModel;
import br.com.common.service.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class AcessoService {

    private static final String BASE_URL = "http://127.0.0.1:8080/api/acessos";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson;

    public AcessoService() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public List<AcessoModel> buscarTodosAcessos() throws IOException {
        Request request = new Request.Builder().url(BASE_URL).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao buscar acessos: " + response);
            }
            if (response.body() == null) {
                return Collections.emptyList();
            }

            Type listType = new TypeToken<List<AcessoModel>>() {}.getType();
            return gson.fromJson(response.body().string(), listType);
        }
    }

    public AcessoModel buscarAcessoPorId(Long id) throws IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + id).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao buscar acesso com ID " + id + ": " + response);
            }
            if (response.body() == null) {
                return null;
            }
            return gson.fromJson(response.body().string(), AcessoModel.class);
        }
    }

    public AcessoModel salvarAcesso(AcessoModel acesso) throws IOException {
        String json = gson.toJson(acesso);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request.Builder requestBuilder = new Request.Builder();
        if (acesso.getId() == null) {
            // Criar (POST)
            requestBuilder.url(BASE_URL).post(body);
        } else {
            // Atualizar (PUT)
            requestBuilder.url(BASE_URL + "/" + acesso.getId()).put(body);
        }

        try (Response response = client.newCall(requestBuilder.build()).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Nenhuma informação de erro.";
                throw new IOException("Erro ao salvar acesso: " + response.code() + " - " + errorBody);
            }
            String responseBody = response.body() != null ? response.body().string() : null;
            return gson.fromJson(responseBody, AcessoModel.class);
        }
    }

    public void deletarAcesso(Long id) throws IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + id).delete().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao deletar acesso com ID " + id + ": " + response);
            }
        }
    }
}
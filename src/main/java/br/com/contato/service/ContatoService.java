package br.com.contato.service;

import br.com.contato.model.ContatoModel;
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

public class ContatoService {

    private static final String BASE_URL = "http://127.0.0.1:8080/api/contatos";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson;

    public ContatoService() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public List<ContatoModel> buscarTodosContatos() throws IOException {
        Request request = new Request.Builder().url(BASE_URL).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao buscar contatos: " + response);
            }
            if (response.body() == null) {
                return Collections.emptyList();
            }

            Type listType = new TypeToken<List<ContatoModel>>() {}.getType();
            return gson.fromJson(response.body().string(), listType);
        }
    }

    public ContatoModel buscarContatoPorId(Long id) throws IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + id).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao buscar contato com ID " + id + ": " + response);
            }
            if (response.body() == null) {
                return null;
            }
            return gson.fromJson(response.body().string(), ContatoModel.class);
        }
    }

    public ContatoModel salvarContato(ContatoModel contato) throws IOException {
        String json = gson.toJson(contato);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request.Builder requestBuilder = new Request.Builder();
        if (contato.getId() == null) {
            // Criar (POST)
            requestBuilder.url(BASE_URL).post(body);
        } else {
            // Atualizar (PUT)
            requestBuilder.url(BASE_URL + "/" + contato.getId()).put(body);
        }

        try (Response response = client.newCall(requestBuilder.build()).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Nenhuma informação de erro.";
                throw new IOException("Erro ao salvar contato: " + response.code() + " - " + errorBody);
            }
            String responseBody = response.body() != null ? response.body().string() : null;
            return gson.fromJson(responseBody, ContatoModel.class);
        }
    }

    public void deletarContato(Long id) throws IOException {
        Request request = new Request.Builder().url(BASE_URL + "/" + id).delete().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao deletar contato com ID " + id + ": " + response);
            }
        }
    }
}
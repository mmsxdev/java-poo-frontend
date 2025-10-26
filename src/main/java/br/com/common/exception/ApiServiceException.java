package br.com.common.exception;

import java.io.IOException;

/**
 * Uma exceção customizada para representar erros ocorridos durante a comunicação
 * com a API do backend.
 */
public class ApiServiceException extends IOException {

    private final int statusCode;

    public ApiServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        // Formata a mensagem de erro para ser mais informativa.
        return "Erro na API (HTTP " + statusCode + "): " + super.getMessage();
    }
}
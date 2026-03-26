package com.gerenciador.tarefas.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Deve retornar mensagem padrão quando não for enum")
    void deveRetornarMensagemPadraoQuandoNaoForEnum() {
        InvalidFormatException mockCause = mock(InvalidFormatException.class);

        when(mockCause.getTargetType()).thenReturn((Class) String.class);

        HttpMessageNotReadableException mockEx = mock(HttpMessageNotReadableException.class);
        when(mockEx.getCause()).thenReturn(mockCause);

        var response = handler.handleMessageNotReadable(mockEx);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Valor inválido no corpo da requisição.", response.getBody().get("message"));
    }
}
package com.gerenciador.tarefas.dtos.response;

import java.util.List;

public record PageResponseDTO<T>(
        List<T> conteudo,
        int pagina,
        int tamanho,
        long totalElementos,
        int totalPaginas
) {}

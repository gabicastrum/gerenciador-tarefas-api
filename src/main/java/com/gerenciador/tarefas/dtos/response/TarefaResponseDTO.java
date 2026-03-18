package com.gerenciador.tarefas.dtos.response;

import com.gerenciador.tarefas.domain.StatusTarefa;

import java.time.LocalDateTime;

public record TarefaResponseDTO(
        Long id,
        String titulo,
        String descricao,
        StatusTarefa statusTarefa,
        LocalDateTime dataCriacao
) {
}

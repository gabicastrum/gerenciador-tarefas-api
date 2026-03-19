package com.gerenciador.tarefas.dtos.request;

import com.gerenciador.tarefas.domain.StatusTarefa;

public record TarefaUpdateRequestDTO(
        String titulo,
        String descricao,
        StatusTarefa statusTarefa
) {
}

package com.gerenciador.tarefas.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record TarefaRequestDTO(
        @NotBlank String titulo,
        String descricao
) {
}

package com.gerenciador.tarefas.mapper;

import com.gerenciador.tarefas.domain.Tarefa;
import com.gerenciador.tarefas.dtos.request.TarefaRequestDTO;
import com.gerenciador.tarefas.dtos.response.TarefaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TarefaMapper {

    Tarefa toEntity(TarefaRequestDTO dto);

    TarefaResponseDTO toDto(Tarefa tarefa);
}

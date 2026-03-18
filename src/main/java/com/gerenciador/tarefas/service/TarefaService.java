package com.gerenciador.tarefas.service;

import com.gerenciador.tarefas.domain.Tarefa;
import com.gerenciador.tarefas.dtos.request.TarefaRequestDTO;
import com.gerenciador.tarefas.dtos.response.TarefaResponseDTO;
import com.gerenciador.tarefas.mapper.TarefaMapper;
import com.gerenciador.tarefas.repository.TarefaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaMapper tarefaMapper;

    @Transactional
    public TarefaResponseDTO criarTarefa(TarefaRequestDTO dto) {
        Tarefa tarefa = tarefaMapper.toEntity(dto);

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        return tarefaMapper.toDto(tarefaSalva);
    }
}

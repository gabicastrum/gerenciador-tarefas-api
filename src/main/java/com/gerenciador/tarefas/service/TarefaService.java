package com.gerenciador.tarefas.service;

import com.gerenciador.tarefas.domain.Tarefa;
import com.gerenciador.tarefas.dtos.request.TarefaRequestDTO;
import com.gerenciador.tarefas.dtos.request.TarefaUpdateRequestDTO;
import com.gerenciador.tarefas.dtos.response.PageResponseDTO;
import com.gerenciador.tarefas.dtos.response.TarefaResponseDTO;
import com.gerenciador.tarefas.exception.TarefaException;
import com.gerenciador.tarefas.mapper.TarefaMapper;
import com.gerenciador.tarefas.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaMapper tarefaMapper;

    @Transactional
    public TarefaResponseDTO criarTarefa(TarefaRequestDTO dto) {
        try {
            Tarefa tarefa = tarefaMapper.toEntity(dto);

            Tarefa tarefaSalva = tarefaRepository.save(tarefa);

            return tarefaMapper.toDto(tarefaSalva);
        } catch (Exception e) {
            throw new TarefaException("Erro ao criar tarefa: " + e.getMessage());
        }
    }

    public PageResponseDTO<TarefaResponseDTO> listarTarefas(Pageable pageable) {
        try {
            Page<Tarefa> page = tarefaRepository.findAll(pageable);

            return new PageResponseDTO<>(
                    page.getContent().stream().map(tarefaMapper::toDto).toList(),
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages()
            );

        } catch (Exception e) {
            throw new TarefaException("Erro ao listar tarefas");
        }
    }

    public TarefaResponseDTO buscarTarefa(Long id) {
        return tarefaMapper.toDto(buscarTarefaPorId(id));
    }

    @Transactional
    public TarefaResponseDTO atualizarDadosTarefa(Long id, TarefaUpdateRequestDTO dto) {
        Tarefa tarefa = buscarTarefaPorId(id);

        tarefa.atualizarDados(
                dto.titulo() != null ? dto.titulo() : tarefa.getTitulo(),
                dto.descricao() != null ? dto.descricao() : tarefa.getDescricao(),
                dto.statusTarefa() != null ? dto.statusTarefa() : tarefa.getStatusTarefa()
        );

        return tarefaMapper.toDto(tarefa);
    }

    @Transactional
    public void deletarTarefa(Long id) {
        buscarTarefaPorId(id);
        tarefaRepository.deleteById(id);
    }

    private Tarefa  buscarTarefaPorId(Long id) {
        return tarefaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada"));
    }
}

package com.gerenciador.tarefas.service;

import com.gerenciador.tarefas.domain.StatusTarefa;
import com.gerenciador.tarefas.domain.Tarefa;
import com.gerenciador.tarefas.dtos.request.TarefaRequestDTO;
import com.gerenciador.tarefas.dtos.request.TarefaUpdateRequestDTO;
import com.gerenciador.tarefas.dtos.response.PageResponseDTO;
import com.gerenciador.tarefas.dtos.response.TarefaResponseDTO;
import com.gerenciador.tarefas.exception.TarefaException;
import com.gerenciador.tarefas.mapper.TarefaMapper;
import com.gerenciador.tarefas.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {
        private static final String TITULO_INICIAL = "Estudar testes unitarios";
        private static final String DESCRICAO_INICIAL = "Aprender JUnit 5";
        private static final Long ID_01 = 1L;
        private static final Long ID_INEXISTENTE = 99L;

        private static final String TITULO_ANTIGO = "Título Antigo";
        private static final String DESCRICAO_ANTIGA = "Descrição Antiga";
        private static final String TITULO_ATUALIZADO = "Título Atualizado";

        private static final String ERRO_LISTAGEM = "Erro ao listar tarefas";
        private static final String ERRO_BANCO_SIMULADO = "Erro SQL";
        private static final String ERRO_TAREFA_NAO_ENCONTRADA = "Tarefa não encontrada";



    @InjectMocks
    private TarefaService tarefaService;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaMapper tarefaMapper;

    @Nested
    @DisplayName("criarTarefa")
    class CriarTarefa {
        @Test
        @DisplayName("Deve criar uma tarefa com sucesso")
        void deveCriarUmaTarefaComSucesso() {
            var requestDTO = new TarefaRequestDTO(TITULO_INICIAL, DESCRICAO_INICIAL);
            var tarefaEntity = new Tarefa(TITULO_INICIAL, DESCRICAO_INICIAL);
            var responseDTO = criarTarefaResponseDTO();

            when(tarefaMapper.toEntity(requestDTO)).thenReturn(tarefaEntity);
            when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaEntity);
            when(tarefaMapper.toDto(tarefaEntity)).thenReturn(responseDTO);

            TarefaResponseDTO resultado = tarefaService.criarTarefa(requestDTO);

            assertNotNull(resultado);
            assertEquals(TITULO_INICIAL, resultado.titulo());
            assertEquals(StatusTarefa.PENDENTE, resultado.statusTarefa());

            verify(tarefaMapper).toEntity(requestDTO);
            verify(tarefaRepository).save(tarefaEntity);
            verify(tarefaMapper).toDto(tarefaEntity);
        }

        @Test
        @DisplayName("Deve lançar exceção ao falhar ao salvar tarefa")
        void deveLancarExcecaoAoSalvarTarefa() {
            var requestDTO = new TarefaRequestDTO(TITULO_INICIAL, DESCRICAO_INICIAL);
            var tarefaEntity = new Tarefa(TITULO_INICIAL, DESCRICAO_INICIAL);

            when(tarefaMapper.toEntity(requestDTO)).thenReturn(tarefaEntity);
            when(tarefaRepository.save(any(Tarefa.class))).thenThrow(new RuntimeException(ERRO_BANCO_SIMULADO));

            assertThrows(TarefaException.class, () -> tarefaService.criarTarefa(requestDTO));

            verify(tarefaRepository).save(tarefaEntity);
            verify(tarefaMapper, never()).toDto(any());
        }
    }
    @Nested
    @DisplayName("listarTarefas")
    class ListarTarefa {
        @Test
        @DisplayName("Deve listar tarefas paginadas com sucesso")
        void deveListarTarefasComSucesso() {
            Pageable pageable = PageRequest.of(0, 10);
            var tarefaEntity = new Tarefa(TITULO_INICIAL, DESCRICAO_INICIAL);
            var tarefaResponse = criarTarefaResponseDTO();
            Page<Tarefa> page = new PageImpl<>(List.of(tarefaEntity), pageable, 1);

            when(tarefaRepository.findAll(pageable)).thenReturn(page);
            when(tarefaMapper.toDto(tarefaEntity)).thenReturn(tarefaResponse);

            PageResponseDTO<TarefaResponseDTO> resultado = tarefaService.listarTarefas(pageable);

            assertNotNull(resultado);
            assertEquals(1, resultado.conteudo().size());
            assertEquals(TITULO_INICIAL, resultado.conteudo().get(0).titulo());
            assertEquals(10, resultado.tamanho());

            verify(tarefaRepository).findAll(pageable);
        }

        @Test
        @DisplayName("Deve lançar exceção ao falhar ao listar tarefas")
        void deveLancarExcecaoAoListarTarefas() {
            Pageable pageable = PageRequest.of(0, 10);
            when(tarefaRepository.findAll(pageable)).thenThrow(new TarefaException(ERRO_LISTAGEM));

            assertThrows(TarefaException.class, () -> tarefaService.listarTarefas(pageable));

            verify(tarefaRepository).findAll(pageable);
        }
    }
    @Nested
    @DisplayName("buscarTarefaPorId")
    class BuscarTarefaPorId {
        @Test
        @DisplayName("Deve buscar tarefa por id com sucesso")
        void deveBuscarTarefaPorIdComSucesso() {
            var tarefaEntity = new Tarefa(TITULO_INICIAL, DESCRICAO_INICIAL);
            var tarefaResponse = criarTarefaResponseDTO();

            when(tarefaRepository.findById(ID_01)).thenReturn(Optional.of(tarefaEntity));
            when(tarefaMapper.toDto(tarefaEntity)).thenReturn(tarefaResponse);

            TarefaResponseDTO resultado = tarefaService.buscarTarefa(ID_01);

            assertNotNull(resultado);
            assertEquals(ID_01, resultado.id());
            assertEquals(TITULO_INICIAL, resultado.titulo());

            verify(tarefaRepository).findById(ID_01);
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException quando a tarefa não existir")
        void deveLancarExcecaoQuandoTarefaNaoEncontrada() {
            when(tarefaRepository.findById(ID_INEXISTENTE)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> tarefaService.buscarTarefa(ID_INEXISTENTE));

            assertEquals(ERRO_TAREFA_NAO_ENCONTRADA, exception.getMessage());
            verify(tarefaRepository).findById(ID_INEXISTENTE);
        }
    }
    @Nested
    @DisplayName("atualizarDadosTarefa")
    class AtualizarTarefa {

        @Test
        @DisplayName("Deve atualizar apenas o título com sucesso")
        void deveAtualizarApenasTituloComSucesso() {
            var updateDTO = new TarefaUpdateRequestDTO(TITULO_ATUALIZADO, null, null);
            var tarefaExistente = new Tarefa(TITULO_ANTIGO, DESCRICAO_ANTIGA);
            tarefaExistente.setId(ID_01);

            var responseDTO = new TarefaResponseDTO(ID_01, TITULO_ATUALIZADO, DESCRICAO_ANTIGA, StatusTarefa.PENDENTE, LocalDateTime.now());

            when(tarefaRepository.findById(ID_01)).thenReturn(Optional.of(tarefaExistente));
            when(tarefaMapper.toDto(any(Tarefa.class))).thenReturn(responseDTO);

            var resultado = tarefaService.atualizarDadosTarefa(ID_01, updateDTO);

            assertEquals(TITULO_ATUALIZADO, resultado.titulo());
            assertEquals(DESCRICAO_ANTIGA, resultado.descricao());

            verify(tarefaRepository).findById(ID_01);
            verify(tarefaMapper).toDto(tarefaExistente);
        }

        @Test
        @DisplayName("Deve concluir tarefa ao atualizar status para CONCLUIDA")
        void deveConcluirTarefaAoAtualizarStatus() {
            var updateDTO = new TarefaUpdateRequestDTO(null, null, StatusTarefa.CONCLUIDA);
            var tarefaExistente = new Tarefa(TITULO_INICIAL, DESCRICAO_INICIAL);

            when(tarefaRepository.findById(ID_01)).thenReturn(Optional.of(tarefaExistente));
            when(tarefaMapper.toDto(any())).thenReturn(mock(TarefaResponseDTO.class));

            tarefaService.atualizarDadosTarefa(ID_01, updateDTO);

            assertEquals(StatusTarefa.CONCLUIDA, tarefaExistente.getStatusTarefa());
            assertNotNull(tarefaExistente.getDataConclusao());
        }
    }
    private TarefaResponseDTO criarTarefaResponseDTO() {
        return new TarefaResponseDTO(
                ID_01,
                TITULO_INICIAL,
                DESCRICAO_INICIAL,
                StatusTarefa.PENDENTE,
                LocalDateTime.now()
        );
    }
}

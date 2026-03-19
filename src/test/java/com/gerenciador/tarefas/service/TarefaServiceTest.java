package com.gerenciador.tarefas.service;

import com.gerenciador.tarefas.domain.StatusTarefa;
import com.gerenciador.tarefas.domain.Tarefa;
import com.gerenciador.tarefas.dtos.request.TarefaRequestDTO;
import com.gerenciador.tarefas.dtos.response.PageResponseDTO;
import com.gerenciador.tarefas.dtos.response.TarefaResponseDTO;
import com.gerenciador.tarefas.exception.TarefaException;
import com.gerenciador.tarefas.mapper.TarefaMapper;
import com.gerenciador.tarefas.repository.TarefaRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    private static final String TITULO_INICIAL = "Estudar testes unitarios";
    private static final String DESCRICAO_INICIAL = "Aprender JUnit 5";
    private static final Long ID_01 = 1L;

    private static final String ERRO_LISTAGEM = "Erro ao listar tarefas";
    private static final String ERRO_BANCO_SIMULADO = "Erro SQL";


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

            assertThrows(TarefaException.class, () -> {
                tarefaService.criarTarefa(requestDTO);
            });

            verify(tarefaRepository).save(tarefaEntity);
            verify(tarefaMapper, never()).toDto(any());
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

                PageResponseDTO<TarefaResponseDTO> resultado =
                        tarefaService.listarTarefas(pageable);

                assertNotNull(resultado);
                assertEquals(1, resultado.conteudo().size());
                assertEquals(TITULO_INICIAL, resultado.conteudo().get(0).titulo());
                assertEquals(0, resultado.pagina());
                assertEquals(10, resultado.tamanho());

                verify(tarefaRepository).findAll(pageable);
                verify(tarefaMapper).toDto(tarefaEntity);
            }

            @Test
            @DisplayName("Deve lançar exceção ao falhar ao listar tarefas")
            void deveLancarExcecaoAoListarTarefas() {
                Pageable pageable = PageRequest.of(0, 10);

                when(tarefaRepository.findAll(pageable))
                        .thenThrow(new TarefaException(ERRO_LISTAGEM));

                assertThrows(TarefaException.class, () -> {
                    tarefaService.listarTarefas(pageable);
                });

                verify(tarefaRepository).findAll(pageable);

                verify(tarefaMapper, never()).toDto(any());
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
}

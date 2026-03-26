package com.gerenciador.tarefas.mapper;

import com.gerenciador.tarefas.domain.StatusTarefa;
import com.gerenciador.tarefas.domain.Tarefa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class TarefaMapperTest {

    private final TarefaMapper mapper = Mappers.getMapper(TarefaMapper.class);

    @Test
    @DisplayName("Deve retornar null quando converter Entidade nula para DTO")
    void deveRetornarNullAoConverterEntidadeNula() {
        assertNull(mapper.toDto(null));
    }

    @Test
    @DisplayName("Deve retornar null quando converter DTO nulo para Entidade")
    void deveRetornarNullAoConverterDtoNulo() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Deve mapear todos os campos de Entidade para DTO com sucesso")
    void deveMapearCamposComSucesso() {
        Tarefa tarefa = new Tarefa("Título", "Descrição");
        tarefa.setId(1L);
        tarefa.setStatusTarefa(StatusTarefa.PENDENTE);

        var dto = mapper.toDto(tarefa);

        assertAll(
                () -> assertEquals(tarefa.getId(), dto.id()),
                () -> assertEquals(tarefa.getTitulo(), dto.titulo()),
                () -> assertEquals(tarefa.getDescricao(), dto.descricao()),
                () -> assertEquals(tarefa.getStatusTarefa(), dto.statusTarefa())
        );
    }
}
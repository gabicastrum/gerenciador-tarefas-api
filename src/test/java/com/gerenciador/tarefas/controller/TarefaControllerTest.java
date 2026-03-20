package com.gerenciador.tarefas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciador.tarefas.domain.StatusTarefa;
import com.gerenciador.tarefas.dtos.request.TarefaRequestDTO;
import com.gerenciador.tarefas.dtos.request.TarefaUpdateRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TarefaControllerTest {

    private static final String URL_TAREFAS = "/tarefas";
    private static final String TITULO_PADRAO = "Tarefa de teste";
    private static final String DESCRICAO_PADRAO = "Descrição da tarefa";
    private static final String TITULO_ATUALIZADO = "Tarefa atualizada";
    private static final Long ID_INEXISTENTE = 999L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("criarTarefa")
    class CriarTarefa {

        @Test
        @DisplayName("Deve criar tarefa com sucesso")
        void deveCriarTarefaComSucesso() throws Exception {
            TarefaRequestDTO request = new TarefaRequestDTO(TITULO_PADRAO, DESCRICAO_PADRAO);

            mockMvc.perform(post(URL_TAREFAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.titulo").value(TITULO_PADRAO))
                    .andExpect(jsonPath("$.descricao").value(DESCRICAO_PADRAO))
                    .andExpect(jsonPath("$.statusTarefa").value(StatusTarefa.PENDENTE.name()));
        }

        @Test
        @DisplayName("Não deve criar tarefa com título em branco")
        void naoDeveCriarTarefaSemTitulo() throws Exception {
            TarefaRequestDTO request = new TarefaRequestDTO("", DESCRICAO_PADRAO);

            mockMvc.perform(post(URL_TAREFAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("listarTarefas")
    class ListarTarefas {

        @Test
        @DisplayName("Deve retornar lista de tarefas")
        void deveRetornarListaDeTarefas() throws Exception {
            mockMvc.perform(post(URL_TAREFAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new TarefaRequestDTO(TITULO_PADRAO, DESCRICAO_PADRAO))))
                    .andExpect(status().isCreated());

            mockMvc.perform(get(URL_TAREFAS)
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.conteudo").isArray());
        }

        @Test
        @DisplayName("Deve filtrar tarefas por status")
        void deveFiltrarTarefasPorStatus() throws Exception {
            mockMvc.perform(post(URL_TAREFAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new TarefaRequestDTO(TITULO_PADRAO, DESCRICAO_PADRAO))))
                    .andExpect(status().isCreated());

            mockMvc.perform(get(URL_TAREFAS)
                            .param("status", StatusTarefa.PENDENTE.name())
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.conteudo").isArray());
        }
    }

    @Nested
    @DisplayName("buscarTarefa")
    class BuscarTarefa {

        @Test
        @DisplayName("Deve buscar tarefa com sucesso")
        void deveBuscarTarefaComSucesso() throws Exception {
            String response = mockMvc.perform(post(URL_TAREFAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new TarefaRequestDTO(TITULO_PADRAO, DESCRICAO_PADRAO))))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();
            long id = objectMapper.readTree(response).get("id").asLong();

            mockMvc.perform(get(URL_TAREFAS + "/" + id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.titulo").value(TITULO_PADRAO));
        }

        @Test
        @DisplayName("Não deve buscar tarefa inexistente")
        void naoDeveBuscarTarefaInexistente() throws Exception {
            mockMvc.perform(get(URL_TAREFAS + "/" + ID_INEXISTENTE))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("atualizarTarefa")
    class AtualizarTarefa {

        @Test
        @DisplayName("Deve atualizar tarefa com sucesso")
        void deveAtualizarTarefaComSucesso() throws Exception {
            String response = mockMvc.perform(post(URL_TAREFAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new TarefaRequestDTO(TITULO_PADRAO, DESCRICAO_PADRAO))))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();
            long id = objectMapper.readTree(response).get("id").asLong();

            TarefaUpdateRequestDTO update = new TarefaUpdateRequestDTO(TITULO_ATUALIZADO, null, StatusTarefa.CONCLUIDA);

            mockMvc.perform(patch(URL_TAREFAS + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(update)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.titulo").value(TITULO_ATUALIZADO))
                    .andExpect(jsonPath("$.statusTarefa").value(StatusTarefa.CONCLUIDA.name()));
        }

        @Test
        @DisplayName("Não deve atualizar tarefa inexistente")
        void naoDeveAtualizarTarefaInexistente() throws Exception {
            TarefaUpdateRequestDTO update = new TarefaUpdateRequestDTO(TITULO_ATUALIZADO, null, null);

            mockMvc.perform(patch(URL_TAREFAS + "/" + ID_INEXISTENTE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(update)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("deletarTarefa")
    class DeletarTarefa {

        @Test
        @DisplayName("Deve deletar tarefa com sucesso")
        void deveDeletarTarefaComSucesso() throws Exception {
            String response = mockMvc.perform(post(URL_TAREFAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new TarefaRequestDTO(TITULO_PADRAO, DESCRICAO_PADRAO))))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();
            long id = objectMapper.readTree(response).get("id").asLong();

            mockMvc.perform(delete(URL_TAREFAS + "/" + id))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Não deve deletar tarefa inexistente")
        void naoDeveDeletarTarefaInexistente() throws Exception {
            mockMvc.perform(delete(URL_TAREFAS + "/" + ID_INEXISTENTE))
                    .andExpect(status().isNotFound());
        }
    }
}
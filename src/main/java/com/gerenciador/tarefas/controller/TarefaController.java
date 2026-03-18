package com.gerenciador.tarefas.controller;

import com.gerenciador.tarefas.domain.Tarefa;
import com.gerenciador.tarefas.dtos.request.TarefaRequestDTO;
import com.gerenciador.tarefas.dtos.response.TarefaResponseDTO;
import com.gerenciador.tarefas.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@RequestBody @Valid TarefaRequestDTO tarefaRequestDTO){
        TarefaResponseDTO tarefaResponseDTO = tarefaService.criarTarefa(tarefaRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tarefaResponseDTO);
    }
}

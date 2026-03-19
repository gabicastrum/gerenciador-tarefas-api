package com.gerenciador.tarefas.controller;

import com.gerenciador.tarefas.domain.StatusTarefa;
import com.gerenciador.tarefas.dtos.request.TarefaRequestDTO;
import com.gerenciador.tarefas.dtos.request.TarefaUpdateRequestDTO;
import com.gerenciador.tarefas.dtos.response.PageResponseDTO;
import com.gerenciador.tarefas.dtos.response.TarefaResponseDTO;
import com.gerenciador.tarefas.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public PageResponseDTO<TarefaResponseDTO> listarTarefas(
            @RequestParam(required = false) StatusTarefa status,
            @PageableDefault(sort = "dataCriacao", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return tarefaService.listarTarefas(status, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> buscarTarefa(@PathVariable Long id){
        TarefaResponseDTO responseDTO = tarefaService.buscarTarefa(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> atualizarTarefa(
            @PathVariable Long id,
            @RequestBody @Valid TarefaUpdateRequestDTO dto
    ) {
        TarefaResponseDTO responseDTO = tarefaService.atualizarDadosTarefa(id, dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id){
        tarefaService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }
}

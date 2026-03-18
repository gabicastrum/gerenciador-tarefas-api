package com.gerenciador.tarefas.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa statusTarefa;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataConclusao;

    public Tarefa(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public void concluir() {
        this.statusTarefa = StatusTarefa.CONCLUIDA;
        this.dataConclusao = LocalDateTime.now();
    }

    public void atualizarDados(String titulo, String descricao, StatusTarefa status) {
        this.titulo = titulo;
        this.descricao = descricao;
        atualizarStatus(status);
    }

    public void atualizarStatus(StatusTarefa status) {
        if (status == StatusTarefa.CONCLUIDA) {
            concluir();
        } else {
            this.statusTarefa = status;
            this.dataConclusao = null;
        }
    }

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        this.statusTarefa = StatusTarefa.PENDENTE;
    }
}

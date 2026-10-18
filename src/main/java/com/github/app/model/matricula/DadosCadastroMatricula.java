package com.github.app.model.matricula;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroMatricula(

    @NotNull
    Integer idAluno,

    @NotNull
    Integer professorId,

    @NotNull
    Turno turno,

    String semestre,

    String observacao,

    LocalDateTime dataMatricula
) {
}

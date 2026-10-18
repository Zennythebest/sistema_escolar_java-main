package com.github.app.model.professor;

import com.github.app.model.endereco.DadosCadastroEndereco;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroProfessor(

    @NotBlank
    String nome,

    @Email
    @NotBlank
    String email,

    String telefone,

    @NotBlank
    String registro,

    @NotNull
    Disciplina disciplina,

    @NotNull @Valid
    DadosCadastroEndereco endereco
) {
}

package com.github.app.model.aluno;

import com.github.app.model.endereco.DadosCadastroEndereco;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroAluno(

    @NotBlank
    String nome,

    @Email
    @NotBlank
    String email,

    String telefone,

    @NotBlank
    String ra,

    @NotBlank
    String curso,

    @NotNull @Valid
    DadosCadastroEndereco endereco
) {
}

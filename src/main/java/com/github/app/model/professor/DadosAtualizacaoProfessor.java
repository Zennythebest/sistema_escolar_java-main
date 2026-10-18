package com.github.app.model.professor;

import com.github.app.model.endereco.DadosCadastroEndereco;

public record DadosAtualizacaoProfessor(
    Integer id,
    String nome,
    String email,
    String telefone,
    DadosCadastroEndereco endereco
) {
}

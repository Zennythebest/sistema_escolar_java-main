package com.github.app.model.aluno;

import com.github.app.model.endereco.DadosCadastroEndereco;

public record DadosAtualizacaoAluno(
    Integer id,
    String nome,
    String telefone,
    String curso,
    DadosCadastroEndereco endereco
) {
}

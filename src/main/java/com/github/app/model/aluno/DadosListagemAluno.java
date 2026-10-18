package com.github.app.model.aluno;

public record DadosListagemAluno(
    Integer id,
    String nome,
    String email,
    String ra,
    String curso
) {
    public DadosListagemAluno(Aluno aluno) {
        this(
            aluno.getId(),
            aluno.getNome(),
            aluno.getEmail(),
            aluno.getRa(),
            aluno.getCurso()
        );
    }
}

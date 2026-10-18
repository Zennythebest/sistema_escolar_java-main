package com.github.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.github.app.model.aluno.AlunoRepository;
import com.github.app.model.aluno.DadosAtualizacaoAluno;
import com.github.app.model.aluno.DadosCadastroAluno;
import com.github.app.model.aluno.DadosListagemAluno;
import com.github.app.model.aluno.Aluno;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroAluno dados) {
        repository.save(new Aluno(dados));
    }

    @GetMapping("/todos")
    public List<Aluno> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/listar")
    public List<DadosListagemAluno> listar() {
        return repository.findAll().stream().map(DadosListagemAluno::new).toList();
    }

    @GetMapping
    public Page<DadosListagemAluno> listarPorPagina(Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemAluno::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody DadosAtualizacaoAluno dados) {
        var aluno = repository.getReferenceById(dados.id());
        aluno.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}

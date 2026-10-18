package com.github.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.github.app.model.professor.DadosAtualizacaoProfessor;
import com.github.app.model.professor.DadosCadastroProfessor;
import com.github.app.model.professor.DadosListagemProfessor;
import com.github.app.model.professor.Professor;
import com.github.app.model.professor.ProfessorRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroProfessor dados) {
        repository.save(new Professor(dados));
    }

    @GetMapping("/todos")
    public List<Professor> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/listar")
    public List<DadosListagemProfessor> listar() {
        return repository.findAll().stream().map(DadosListagemProfessor::new).toList();
    }

    @GetMapping
    public Page<DadosListagemProfessor> listarPorPagina(Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemProfessor::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody DadosAtualizacaoProfessor dados) {
        var professor = repository.getReferenceById(dados.id());
        professor.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}

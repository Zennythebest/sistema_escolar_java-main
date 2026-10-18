package com.github.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.app.model.aluno.Aluno;
import com.github.app.model.aluno.AlunoRepository;
import com.github.app.model.matricula.DadosCadastroMatricula;
import com.github.app.model.matricula.Matricula;
import com.github.app.model.matricula.MatriculaRepository;
import com.github.app.model.professor.Professor;
import com.github.app.model.professor.ProfessorRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaRepository repository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMatricula dados) {
        Aluno aluno = alunoRepository.getReferenceById(dados.idAluno());
        Professor professor = professorRepository.getReferenceById(dados.professorId());
        Matricula matricula = new Matricula(dados);
        matricula.setAluno(aluno);
        matricula.setProfessor(professor);
        repository.save(matricula);
    }

    @GetMapping
    public List<Matricula> listar() {
        return repository.findAll();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}

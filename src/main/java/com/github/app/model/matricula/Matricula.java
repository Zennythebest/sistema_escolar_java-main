package com.github.app.model.matricula;

import java.time.LocalDateTime;

import com.github.app.model.aluno.Aluno;
import com.github.app.model.professor.Professor;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "matriculas")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "aluno_id")
    @ManyToOne
    private Aluno aluno;

    @JoinColumn(name = "professor_id")
    @ManyToOne
    private Professor professor;

    @Enumerated(EnumType.STRING)
    private Turno turno;

    private String semestre;
    private String observacao;
    private LocalDateTime dataMatricula;

    public Matricula(DadosCadastroMatricula dados) {
        this.turno = dados.turno();
        this.semestre = dados.semestre();
        this.observacao = dados.observacao();
        this.dataMatricula = dados.dataMatricula() != null ? dados.dataMatricula() : LocalDateTime.now();
    }
}

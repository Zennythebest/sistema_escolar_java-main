Zeneida Girão

# Sistema Escolar — Spring Boot

Projeto base da atividade **Caça aos Erros** (Programação Web 1 · Senac Rio).

API REST de um sistema escolar de cadastro com **Professor**, **Aluno** e **Matrícula**,
usando Java 17, Spring Boot, Spring Data JPA e banco H2 em memória.

## Como executar

```bash
./mvnw spring-boot:run
```

Com o servidor rodando, teste os endpoints no Insomnia ou no Postman
(`/professores`, `/alunos` e `/matriculas`) e acesse o console do banco em
`http://localhost:8080/h2-console`.

---

# Relatório da Caça aos Erros — 8 erros encontrados e corrigidos

Resumo: 2 erros de configuração, 3 de entidades, 3 de controllers.

| # | Arquivo | Tipo | Quando aparece |
|---|---------|------|----------------|
| 1 | `pom.xml` | Configuração | Ao subir o sistema |
| 2 | `application-test.properties` | Configuração | Ao salvar o primeiro registro |
| 3 | `Professor.java` | Entidade | Ao cadastrar professor |
| 4 | `Aluno.java` | Entidade/lógica | Ao listar alunos (dado errado) |
| 5 | `Matricula.java` | Entidade | Ao matricular o mesmo aluno 2ª vez |
| 6 | `ProfessorController.java` | Controller | Ao chamar `/professores` |
| 7 | `AlunoController.java` | Controller | Ao atualizar aluno (PUT) |
| 8 | `AlunoController`, `ProfessorController`, `MatriculaController` | Controller/DTO | Ao enviar dados inválidos |

## Erro 1 — H2 com escopo `test` (`pom.xml`)
- **O que estava errado:** a dependência `com.h2database:h2` tinha `<scope>test</scope>`, então o driver só existia no classpath dos testes.
- **Como descobri:** a aplicação não sobe com `./mvnw spring-boot:run`; o `application-test.properties` aponta para `jdbc:h2:mem:clinicadb`, mas o driver não está disponível (erro do tipo *"Failed to determine a suitable driver class"* / *Cannot load driver class: org.h2.Driver*).
- **Correção:** trocar para `<scope>runtime</scope>`.

## Erro 2 — `ddl-auto=none` (`src/main/resources/application-test.properties`)
- **O que estava errado:** `spring.jpa.hibernate.ddl-auto=none` faz o Hibernate não criar as tabelas no H2 em memória (que nasce vazio).
- **Como descobri:** o sistema sobe, mas qualquer `POST`/`GET` falha com `Table "PROFESSORES" not found` / `Table "ALUNOS" not found`.
- **Correção:** `spring.jpa.hibernate.ddl-auto=update`.

## Erro 3 — `Professor` sem geração de ID (`model/professor/Professor.java`)
- **O que estava errado:** o campo `id` tinha só `@Id`, sem `@GeneratedValue`. Aluno e Matrícula usam `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
- **Como descobri:** `POST /professores` retorna 500 com `ids for this class must be manually assigned before calling save()` (ou `id` nulo/duplicado).
- **Correção:** adicionar `@GeneratedValue(strategy = GenerationType.IDENTITY)`.

## Erro 4 — RA do aluno recebendo o curso (`model/aluno/Aluno.java`)
- **O que estava errado:** no construtor, `this.ra = dados.curso();` em vez de `dados.ra()`.
- **Como descobri:** comportamento estranho, sem erro: ao cadastrar um aluno com `ra: "2024001"` e `curso: "ADS"`, o `GET /alunos/listar` devolvia `ra: "ADS"`.
- **Correção:** `this.ra = dados.ra();`.

## Erro 5 — Relacionamento Matrícula → Aluno como `@OneToOne` (`model/matricula/Matricula.java`)
- **O que estava errado:** `@OneToOne` em `aluno` cria uma restrição de unicidade em `aluno_id`, ou seja, cada aluno só poderia ter uma matrícula. O correto é muitas matrículas para um aluno (`@ManyToOne`), como já era feito com Professor.
- **Como descobri:** a primeira matrícula funciona; ao criar uma segunda matrícula para o mesmo aluno, ocorre violação de unicidade (`Unique index or primary key violation`).
- **Correção:** trocar por `@ManyToOne`.

## Erro 6 — Rota errada em `ProfessorController` (`controller/ProfessorController.java`)
- **O que estava errado:** `@RequestMapping("/professor")` (singular), enquanto o README e os outros controllers usam plural (`/alunos`, `/matriculas`) e se espera `/professores`.
- **Como descobri:** `GET /professores` retorna **404 Not Found**.
- **Correção:** `@RequestMapping("/professores")`.

## Erro 7 — `PUT /alunos` sem `@Transactional` (`controller/AlunoController.java`)
- **O que estava errado:** o método `atualizar` altera a entidade gerenciada (`getReferenceById` + `atualizarInformacoes`) mas não tinha `@Transactional`; sem transação, o JPA não faz o *flush* da alteração. O `ProfessorController` tem a anotação.
- **Como descobri:** `PUT /alunos` responde 200, mas o `GET` seguinte mostra os dados antigos (a alteração não persiste).
- **Correção:** adicionar `@Transactional` ao método.

## Erro 8 — Validações dos DTOs nunca executadas (`AlunoController`, `ProfessorController`, `MatriculaController`)
- **O que estava errado:** os DTOs de cadastro têm `@NotBlank`, `@Email`, `@NotNull`, `@Valid` (e o `pom.xml` tem o starter de validation), mas os métodos `cadastrar` recebiam `@RequestBody` sem `@Valid`, então nada era validado.
- **Como descobri:** `POST /alunos` com nome vazio e e-mail inválido (`"abc"`) é aceito e salvo, em vez de retornar **400 Bad Request**.
- **Correção:** `@RequestBody @Valid ...` nos três `cadastrar` (com `import jakarta.validation.Valid`).

---

## Observação extra — `/h2-console` retornava 404 (não é um dos 8 erros)

- **Sintoma:** com o sistema no ar, `http://localhost:8080/h2-console` mostrava `NoResourceFoundException: No static resource h2-console`.
- **Causa:** a partir do Spring Boot 4 a autoconfiguração do console do H2 saiu do módulo principal e ficou em `spring-boot-h2console`. Sem esse módulo, a rota não é registrada (e o Spring tenta servir `/h2-console` como arquivo estático).
- **Correção:** adicionar ao `pom.xml` a dependência `org.springframework.boot:spring-boot-h2console` (scope `runtime`).
- **Para conectar no console:** JDBC URL `jdbc:h2:mem:clinicadb`, usuário `sa`, senha vazia.

---


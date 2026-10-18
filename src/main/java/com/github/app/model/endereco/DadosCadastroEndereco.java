package com.github.app.model.endereco;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroEndereco(

    @NotBlank
    String logradouro,

    @NotBlank
    String bairro,

    @NotBlank
    String cep,

    String complemento,

    @NotBlank
    String cidade,

    @NotBlank
    String uf

) {

}

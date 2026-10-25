// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import api.model.TipoOcorrencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// CAMADA: DTO
// RESPONSABILIDADE: o JSON que o cliente envia para cadastrar um órgão.
//                   O cliente não envia id nem "ativo": quem decide é a API.
// SITUAÇÃO: implementada.
public record OrgaoRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String nome,

        @NotNull(message = "Tipo atendido é obrigatório")
        TipoOcorrencia tipoAtendido
) {
}

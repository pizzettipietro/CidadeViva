// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// CAMADA: DTO
// RESPONSABILIDADE: o JSON que o cliente envia para cadastrar um cidadão.
// SITUAÇÃO: implementada.
public record CidadaoRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 150, message = "E-mail deve ter no máximo 150 caracteres")
        String email
) {
}

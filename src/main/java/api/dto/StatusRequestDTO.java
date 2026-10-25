// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import api.model.StatusOcorrencia;
import jakarta.validation.constraints.NotNull;

// CAMADA: DTO
// RESPONSABILIDADE: o JSON que o órgão envia para mudar o status de uma
//                   ocorrência. O orgaoId diz QUEM está pedindo (a R3 confere
//                   se é o órgão responsável). Sem login nesta etapa.
// SITUAÇÃO: implementada.
public record StatusRequestDTO(

        @NotNull(message = "Órgão é obrigatório")
        Long orgaoId,

        @NotNull(message = "Novo status é obrigatório")
        StatusOcorrencia novoStatus
) {
}

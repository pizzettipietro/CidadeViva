// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import api.model.TipoOcorrencia;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// CAMADA: DTO
// RESPONSABILIDADE: o JSON que o cidadão envia para reportar um problema.
//                   O cliente NÃO envia órgão, status, data nem contagem de
//                   reportes: a API decide (R1 e R2).
// SITUAÇÃO: implementada.
public record OcorrenciaRequestDTO(

        @NotNull(message = "Cidadão é obrigatório")
        Long cidadaoId,

        @NotNull(message = "Tipo é obrigatório")
        TipoOcorrencia tipo,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotNull(message = "Latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
        @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
        Double latitude,

        @NotNull(message = "Longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
        @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
        Double longitude
) {
}

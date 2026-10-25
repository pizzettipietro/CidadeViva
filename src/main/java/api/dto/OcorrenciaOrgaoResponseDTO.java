// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import java.time.LocalDateTime;

import api.model.StatusOcorrencia;
import api.model.TipoOcorrencia;

// CAMADA: DTO
// RESPONSABILIDADE: a ocorrência como o ÓRGÃO enxerga: os mesmos campos da visão
//                   do cidadão, mais quantidadeReportes (quantas pessoas
//                   reportaram o mesmo problema, somadas pela R2).
// SITUAÇÃO: implementada.
public record OcorrenciaOrgaoResponseDTO(
        Long id,
        TipoOcorrencia tipo,
        String descricao,
        double latitude,
        double longitude,
        StatusOcorrencia status,
        LocalDateTime dataAbertura,
        String orgaoNome,
        int quantidadeReportes
) {
}

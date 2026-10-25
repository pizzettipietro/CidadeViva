// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import java.time.LocalDateTime;

import api.model.StatusOcorrencia;
import api.model.TipoOcorrencia;

// CAMADA: DTO
// RESPONSABILIDADE: a ocorrência como o CIDADÃO enxerga. De propósito, NÃO tem
//                   quantidadeReportes: o campo nem existe aqui, então não
//                   tem como vazar por engano.
// SITUAÇÃO: implementada.
public record OcorrenciaResponseDTO(
        Long id,
        TipoOcorrencia tipo,
        String descricao,
        double latitude,
        double longitude,
        StatusOcorrencia status,
        LocalDateTime dataAbertura,
        String orgaoNome
) {
}

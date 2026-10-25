// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

import api.model.TipoOcorrencia;

// CAMADA: DTO
// RESPONSABILIDADE: o JSON que a API devolve para um órgão.
// SITUAÇÃO: implementada.
public record OrgaoResponseDTO(
        Long id,
        String nome,
        TipoOcorrencia tipoAtendido,
        boolean ativo
) {
}

// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

// CAMADA: DTO
// RESPONSABILIDADE: o JSON que a API devolve para um cidadão.
// SITUAÇÃO: implementada.
public record CidadaoResponseDTO(
        Long id,
        String nome,
        String email
) {
}

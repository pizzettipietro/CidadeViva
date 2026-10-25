// Declara o pacote dos DTOs -- as classes que formam o contrato da API.
package api.dto;

// CAMADA: DTO
// RESPONSABILIDADE: a confirmação que o cidadão recebe ao reportar. É IGUAL
//                   quando a ocorrência é nova e quando o reporte foi agrupado
//                   em uma existente (R2): o cidadão não vê a diferença.
// SITUAÇÃO: implementada.
public record ConfirmacaoOcorrenciaDTO(
        Long protocolo,
        String mensagem
) {
}

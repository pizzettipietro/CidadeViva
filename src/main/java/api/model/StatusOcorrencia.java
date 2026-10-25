// Define o pacote das entidades e enums do domínio.
package api.model;

// CAMADA: Model
// RESPONSABILIDADE: lista fechada das etapas de atendimento de uma ocorrência.
//                   Só o órgão responsável muda o status (R3).
// SITUAÇÃO: implementada.
public enum StatusOcorrencia {
    ABERTA,
    EM_ANALISE,
    EM_ATENDIMENTO,
    RESOLVIDA
}

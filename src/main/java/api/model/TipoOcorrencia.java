// Define o pacote das entidades e enums do domínio.
package api.model;

// CAMADA: Model
// RESPONSABILIDADE: lista fechada dos tipos de problema urbano que o sistema
//                   aceita. Usada na ocorrência (o que foi reportado) e no
//                   órgão (o que ele atende), e é por ela que a R1 liga um ao outro.
// SITUAÇÃO: implementada.
public enum TipoOcorrencia {
    BURACO,
    ILUMINACAO_PUBLICA,
    COLETA_LIXO,
    VAZAMENTO_AGUA,
    SINALIZACAO
}

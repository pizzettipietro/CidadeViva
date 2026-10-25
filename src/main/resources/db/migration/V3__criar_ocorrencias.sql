-- Cria a tabela das ocorrências (os problemas urbanos reportados).
-- Precisa vir depois de orgaos e cidadaos, que ela referencia.
CREATE TABLE ocorrencias (
    -- Chave primária gerada pelo banco. É o protocolo que o cidadão recebe.
    id BIGSERIAL PRIMARY KEY,
    -- Tipo do problema (texto do enum TipoOcorrencia).
    tipo VARCHAR(30) NOT NULL,
    -- Descrição escrita pelo cidadão.
    descricao VARCHAR(500) NOT NULL,
    -- Localização do problema, em graus decimais.
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    -- Andamento do atendimento (texto do enum StatusOcorrencia).
    status VARCHAR(30) NOT NULL,
    -- Momento em que a ocorrência foi aberta.
    data_abertura TIMESTAMP NOT NULL,
    -- Quantas pessoas reportaram este mesmo problema (R2 soma 1 a cada reporte).
    quantidade_reportes INTEGER NOT NULL DEFAULT 1,
    -- Órgão responsável, definido pela regra R1.
    orgao_id BIGINT NOT NULL REFERENCES orgaos (id),
    -- Cidadão que abriu a ocorrência (o primeiro reporte).
    cidadao_id BIGINT NOT NULL REFERENCES cidadaos (id)
);

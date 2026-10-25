-- Cria a tabela dos órgãos competentes (secretarias e autoridades).
-- Cada órgão atende um único tipo de ocorrência (tipo_atendido): é por ele
-- que a regra R1 escolhe para onde a ocorrência vai.
CREATE TABLE orgaos (
    -- Chave primária gerada pelo banco.
    id BIGSERIAL PRIMARY KEY,
    -- Nome do órgão, ex.: "Secretaria de Obras".
    nome VARCHAR(150) NOT NULL,
    -- Tipo de problema que o órgão resolve (texto do enum TipoOcorrencia).
    tipo_atendido VARCHAR(30) NOT NULL,
    -- Órgão inativo não recebe ocorrências novas (R1).
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Cria a tabela dos cidadãos que reportam problemas.
CREATE TABLE cidadaos (
    -- Chave primária gerada pelo banco.
    id BIGSERIAL PRIMARY KEY,
    -- Nome completo do cidadão.
    nome VARCHAR(150) NOT NULL,
    -- E-mail único: dois cidadãos não repetem o e-mail.
    email VARCHAR(150) NOT NULL UNIQUE
);

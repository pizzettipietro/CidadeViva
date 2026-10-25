# Autoavaliação

Os 10 critérios que serão avaliados na revisão do código, descritos em `Aula 10-11/AVALIACAO.md`, no repositório da disciplina. Para cada um, a marca do grupo e onde está a evidência.

- ✅ atende
- ◐ atende em parte (diga o que falta)
- ❌ não atende

| Critério | Marca | Evidência |
|---|---|---|
| P1 A API sobe seguindo o README | | `README.md`, seção "Como executar" |
| P2 Cada classe no pacote da sua camada | | `src/main/java/api/`: `model/`, `repository/`, `dto/`, `mapper/`, `service/` e `controller/` |
| P3 Migrations e `@Entity` de todas as tabelas, e a API sobe | | `V1__criar_orgaos.sql`, `V2__criar_cidadaos.sql` e `V3__criar_ocorrencias.sql` em `db/migration/`; `Orgao`, `Cidadao` e `Ocorrencia` em `model/` |
| P4 DTOs de entrada e de saída de todas as entidades | | `OrgaoRequestDTO`/`OrgaoResponseDTO`, `CidadaoRequestDTO`/`CidadaoResponseDTO`, `OcorrenciaRequestDTO`, `OcorrenciaResponseDTO` e `OcorrenciaOrgaoResponseDTO` em `dto/` |
| P5 Repository, Mapper, Service e Controller de todas, com o comentário no topo, e o mapa da estrutura | | Classes em `repository/`, `mapper/`, `service/` e `controller/`; `README.md`, seção "Arquitetura" |
| P6 API simples: `POST`, `GET` e `GET /{id}`, com `400` e `404` | | `OrgaoController` e `CidadaoController`: `POST`, `GET` e `GET /{id}` |
| P7 No mínimo 3 regras de negócio implementadas, com `400` quando violadas | | `OcorrenciaService`: R1 e R2 em `reportar` (`POST /ocorrencias`), R3 em `mudarStatus` (`PUT /ocorrencias/{id}/status`) |
| P8 As regras atendem ao problema e ao tema | | `README.md`, seção "O problema": encaminhamento ao órgão competente, agrupamento de reportes do mesmo problema e status só pelo órgão responsável |
| P9 Código igual ao desenho | | `docs/modelo-de-dominio.md` e `docs/contrato-da-api.md` |
| P10 README completo e bem escrito | | `README.md`, conferido no GitHub |

**Pontos que o grupo espera no projeto:** {{de 0 a 5,0}}

A apresentação vale os outros 5,0 pontos: são 3 perguntas, feitas na Aula 11.

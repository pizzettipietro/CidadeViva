# CidadeViva

O CidadeViva é uma API para o cidadão reportar problemas urbanos (buraco na rua, lâmpada queimada, lixo acumulado) e acompanhar a resolução. Cada ocorrência é encaminhada automaticamente ao órgão competente, e reportes repetidos do mesmo problema no mesmo local são agrupados em uma só ocorrência.

Projeto da disciplina de Backend (Engenharia de Software, SATC). **Etapa 1: arquitetura do backend.**

## Índice

- [Integrantes](#integrantes)
- [O problema](#o-problema)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Entidades](#entidades)
- [Rotas](#rotas)
- [Como executar](#como-executar)
- [Situação do projeto](#situação-do-projeto)
- [Documentação](#documentação)

## Integrantes

| Nome | GitHub |
|---|---|
| Pietro Medeiros Pizzetti | [@pizzettipietro](https://github.com/pizzettipietro) |
| Vinicius Teixeira Tuxen | [@ViniDizel](https://github.com/ViniDizel) |
| Miguel de Oliveira | [@migueldeoliveira7002-star](https://github.com/migueldeoliveira7002-star) |

## O problema

**Tema sorteado:** 🏙️ Cidades Inteligentes

Hoje, quem vê um buraco, uma lâmpada queimada ou lixo acumulado na rua não sabe a quem avisar. Liga para a prefeitura, é transferido de setor em setor e não recebe protocolo nem retorno. Do lado do poder público, o mesmo problema chega várias vezes, por vários canais, e ninguém sabe se são cinco problemas ou um só reportado por cinco pessoas. O CidadeViva resolve os dois lados: o cidadão informa o tipo e a localização, o sistema encaminha ao órgão certo sem o cidadão precisar saber qual é, e reportes repetidos viram uma única ocorrência com a contagem visível ao órgão.

**Quem usa o sistema:**

| Quem | O que faz no sistema |
|---|---|
| Cidadão | Cadastra-se, reporta ocorrências (tipo e localização) e acompanha o andamento de todas as ocorrências |
| Órgão competente | Recebe as ocorrências do seu tipo, vê quantas pessoas reportaram cada uma e atualiza o status |

**Regras de negócio:**

| # | Regra | Onde está no código | Rota que usa |
|---|---|---|---|
| R1 | A ocorrência é encaminhada automaticamente ao órgão ativo que atende o tipo escolhido. Se não existe órgão ativo para o tipo, o pedido é barrado com `400` | `OcorrenciaService` | `POST /ocorrencias` |
| R2 | Se já existe ocorrência do mesmo tipo, a menos de ~50 m, e ainda não resolvida, o sistema não cria outra: soma 1 em `quantidade_reportes`. O cidadão recebe a mesma confirmação de "ocorrência enviada" e o órgão vê a contagem aumentar | `OcorrenciaService` | `POST /ocorrencias` |
| R3 | Somente o órgão responsável pela ocorrência pode mudar o status dela. Outro órgão recebe `400` | `OcorrenciaService` | `PUT /ocorrencias/{id}/status` |

A quantidade de reportes só aparece para o órgão (`GET /orgaos/{id}/ocorrencias` e a resposta do `PUT`). O cidadão usa um DTO de saída que nem tem esse campo.

**Fora do escopo:**

- Autenticação e login: o órgão informa o próprio `orgaoId` na requisição, e a rota define quem enxerga a contagem. Provar a identidade de verdade fica para as próximas etapas.
- Um mesmo cidadão pode reportar o mesmo problema mais de uma vez e aumentar a contagem, porque esta etapa não guarda quem fez cada reporte.
- Ordem obrigatória dos status (`ABERTA`, `EM_ANALISE`, `EM_ATENDIMENTO`, `RESOLVIDA`): nesta etapa o órgão pode ir para qualquer um.
- Fotos, comentários, notificações e mapa.
- `PUT` e `DELETE` das entidades.

## Tecnologias

| O quê | Qual |
|---|---|
| Linguagem | Java 25 |
| Framework | Spring Boot 3.5 (Web, Validation, Data JPA) |
| Banco de dados | PostgreSQL 16, em Docker |
| Migrations | Flyway |
| Documentação da API | springdoc-openapi (Swagger UI) |

## Arquitetura

A API é organizada em camadas. Uma requisição entra pelo Controller, passa pelo Service, que aplica as regras de negócio, e chega ao banco pelo Repository; a resposta faz o caminho de volta. O Mapper converte DTO em entidade e entidade em DTO.

**Mapa da estrutura.** Uma linha por entidade. Situação de cada classe: **pronta**, **implementada** (já tem lógica), **em parte** (só uma parte das rotas) ou **montada** (criada, ligada às outras, ainda sem lógica).

| Entidade | Migration | Model | Repository | DTOs | Mapper | Service | Controller |
|---|---|---|---|---|---|---|---|
| Orgao | `V1__criar_orgaos.sql`, pronta | `Orgao`, pronta | `OrgaoRepository`, pronto | `OrgaoRequestDTO`, `OrgaoResponseDTO`, prontos | `OrgaoMapper`, implementado | `OrgaoService` e `OrgaoNaoEncontradoException`, implementados | `OrgaoController`, implementado (inclui `GET /orgaos/{id}/ocorrencias`) |
| Cidadao | `V2__criar_cidadaos.sql`, pronta | `Cidadao`, pronta | `CidadaoRepository`, pronto | `CidadaoRequestDTO`, `CidadaoResponseDTO`, prontos | `CidadaoMapper`, implementado | `CidadaoService` e `CidadaoNaoEncontradoException`, implementados | `CidadaoController`, implementado |
| Ocorrencia | `V3__criar_ocorrencias.sql`, pronta | `Ocorrencia`, `TipoOcorrencia` e `StatusOcorrencia`, prontas | `OcorrenciaRepository`, pronto | `OcorrenciaRequestDTO`, `OcorrenciaResponseDTO`, `OcorrenciaOrgaoResponseDTO`, `ConfirmacaoOcorrenciaDTO` e `StatusRequestDTO`, prontos | `OcorrenciaMapper`, implementado | `OcorrenciaService` (R1, R2 e R3) e `OcorrenciaNaoEncontradaException`, implementados | `OcorrenciaController`, implementado |

**Onde está cada regra:** R1, R2 e R3 estão no método `reportar` e no método `mudarStatus` de `OcorrenciaService`. O Controller só repassa a requisição, e o Mapper só converte.

## Entidades

| Entidade | O que representa | Como se relaciona |
|---|---|---|
| Orgao | Secretaria ou autoridade responsável por um tipo de problema (ex.: Secretaria de Obras para buracos) | Recebe várias ocorrências |
| Cidadao | Pessoa que reporta problemas | Abre várias ocorrências |
| Ocorrencia | Um problema urbano em um local, com o status do atendimento e a quantidade de pessoas que o reportaram | Pertence a um órgão e foi aberta por um cidadão |

Atributos, tipos e o diagrama das tabelas estão em [docs/modelo-de-dominio.md](docs/modelo-de-dominio.md).

## Rotas

### As que já funcionam

| Verbo | Caminho | O que faz | Regra | Sucesso | Erros |
|---|---|---|---|---|---|
| `POST` | `/orgaos` | Cadastra um órgão | Nenhuma | `201` | `400` |
| `GET` | `/orgaos` | Lista os órgãos | Nenhuma | `200` | Nenhum |
| `GET` | `/orgaos/{id}` | Busca um órgão | Nenhuma | `200` | `404` |
| `POST` | `/cidadaos` | Cadastra um cidadão (e-mail repetido devolve `400`) | Nenhuma | `201` | `400` |
| `GET` | `/cidadaos` | Lista os cidadãos | Nenhuma | `200` | Nenhum |
| `GET` | `/cidadaos/{id}` | Busca um cidadão | Nenhuma | `200` | `404` |
| `POST` | `/ocorrencias` | Reporta um problema (cria ou agrupa) | R1 e R2 | `201` | `400`, `404` |
| `GET` | `/ocorrencias` | Lista todas as ocorrências (visão do cidadão) | Nenhuma | `200` | Nenhum |
| `GET` | `/ocorrencias/{id}` | Busca uma ocorrência (visão do cidadão) | Nenhuma | `200` | `404` |
| `GET` | `/orgaos/{id}/ocorrencias` | Lista as ocorrências do órgão, com a contagem de reportes | Nenhuma | `200` | `404` |
| `PUT` | `/ocorrencias/{id}/status` | O órgão muda o status | R3 | `200` | `400`, `404` |

Exemplo de requisição:

```http
POST /ocorrencias
Content-Type: application/json

{
  "cidadaoId": 1,
  "tipo": "BURACO",
  "descricao": "Buraco grande na pista, perto do cruzamento",
  "latitude": -28.6775,
  "longitude": -49.3197
}
```

Resposta, `201 Created` (igual para ocorrência nova e para reporte agrupado):

```json
{
  "protocolo": 7,
  "mensagem": "Ocorrência enviada com sucesso"
}
```

### Erros

Todo erro sai no mesmo formato. Este é o `400` do R1, quando nenhum órgão atende o tipo:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R1: não há órgão ativo que atenda o tipo BURACO",
  "caminho": "/ocorrencias",
  "timestamp": "2026-10-26T19:30:00",
  "campos": []
}
```

Um campo inválido também devolve `400`, com a lista `campos` dizendo qual campo corrigir. Um id que não existe devolve `404`.

### As planejadas

Estão no contrato, mas ainda não respondem.

| Recurso | O que vai fazer |
|---|---|
| `PUT` e `DELETE` de órgãos e cidadãos | Atualizar e remover |
| Ordem obrigatória dos status | Impedir pular etapas e reabrir ocorrência resolvida |

O contrato completo, com todas as rotas e os DTOs, está em [docs/contrato-da-api.md](docs/contrato-da-api.md).

## Como executar

Pré-requisitos: JDK 25, Docker Desktop aberto e Git.

```bash
git clone https://github.com/pizzettipietro/CidadeViva.git
cd CidadeViva

docker compose up -d          # sobe o PostgreSQL na porta 5437
./mvnw spring-boot:run        # Windows: .\mvnw.cmd spring-boot:run
```

Com a API no ar:

| Endereço | O que é |
|---|---|
| <http://localhost:8080/swagger-ui.html> | Swagger UI: as rotas, com **Try it out** para enviar requisições |
| <http://localhost:8080/v3/api-docs> | O contrato OpenAPI em JSON |

Para testar as rotas, use o Swagger UI ou o **Postman**. No Postman, importe todas as rotas de uma vez: **Import** → **Link** → `http://localhost:8080/v3/api-docs`.

**Roteiro para ver as regras funcionando:**

1. `POST /orgaos` com `{"nome": "Secretaria de Obras", "tipoAtendido": "BURACO"}`.
2. `POST /cidadaos` duas vezes, com dois cidadãos diferentes.
3. `POST /ocorrencias` com o primeiro cidadão: a ocorrência nasce já ligada à Secretaria de Obras (R1).
4. `POST /ocorrencias` com o segundo cidadão, mesmo tipo e uma coordenada quase igual: recebe a mesma confirmação, mas `GET /orgaos/1/ocorrencias` mostra `quantidadeReportes` igual a 2 (R2).
5. `PUT /ocorrencias/1/status` com o `orgaoId` de outro órgão: `400` (R3). Com o órgão certo: `200`.

Para parar: `Ctrl+C` na API e `docker compose stop` no banco. Para apagar o banco e rodar as migrations do zero: `docker compose down -v` e suba de novo.

## Situação do projeto

| Parte | Situação |
|---|---|
| Desenho | Completo: problema e regras neste README, modelo e contrato em `docs/` |
| Estrutura | As 3 entidades têm migration, entidade, Repository, DTOs, Mapper, Service e Controller, cada classe com o comentário no topo |
| API e regras | As rotas de órgãos, cidadãos e ocorrências funcionam; R1, R2 e R3 estão implementadas em `OcorrenciaService` |

**Ainda não funciona:** `PUT` e `DELETE` de órgãos, cidadãos e ocorrências; a ordem obrigatória dos status.

## Documentação

| Documento | O que tem |
|---|---|
| [Modelo de domínio](docs/modelo-de-dominio.md) | Entidades, relacionamentos, diagrama das tabelas, migrations |
| [Contrato da API](docs/contrato-da-api.md) | Todas as rotas, os DTOs, exemplos de JSON e erros |
| [Autoavaliação](docs/autoavaliacao.md) | Os critérios da revisão do código, marcados pelo grupo |

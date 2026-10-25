# Contrato da API

O contrato é o que o cliente da API enxerga: rotas, JSON e status. Estão desenhadas as rotas de **todas** as entidades. Nesta etapa funcionam as da API simples (órgãos e cidadãos) e as que as regras de negócio usam (ocorrências).

## Convenções

| Item | Valor |
|---|---|
| Endereço base | `http://localhost:8080` |
| Formato | JSON, na requisição e na resposta |
| Datas | `yyyy-MM-dd'T'HH:mm:ss` |
| Erros | Sempre no formato `ErroDTO` (ver [Erros](#erros)) |
| Documentação viva | `http://localhost:8080/swagger-ui.html` |

Valores aceitos:

| Campo | Valores |
|---|---|
| `tipo` e `tipoAtendido` | `BURACO`, `ILUMINACAO_PUBLICA`, `COLETA_LIXO`, `VAZAMENTO_AGUA`, `SINALIZACAO` |
| `status` e `novoStatus` | `ABERTA`, `EM_ANALISE`, `EM_ATENDIMENTO`, `RESOLVIDA` |

## Rotas

Coluna **Funciona**: `sim` para as rotas que já respondem nesta etapa.

### Órgãos

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `GET` | `/orgaos` | Lista os órgãos | Nenhuma | lista de `OrgaoResponseDTO` | `200` | Nenhum | sim |
| `GET` | `/orgaos/{id}` | Busca um órgão | Nenhuma | `OrgaoResponseDTO` | `200` | `404` | sim |
| `POST` | `/orgaos` | Cadastra um órgão | `OrgaoRequestDTO` | `OrgaoResponseDTO` | `201` | `400` | sim |
| `PUT` | `/orgaos/{id}` | Atualiza um órgão | `OrgaoRequestDTO` | `OrgaoResponseDTO` | `200` | `400`, `404` | não |
| `DELETE` | `/orgaos/{id}` | Remove um órgão | Nenhuma | Nenhuma (sem corpo) | `204` | `404` | não |

### Cidadãos

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `GET` | `/cidadaos` | Lista os cidadãos | Nenhuma | lista de `CidadaoResponseDTO` | `200` | Nenhum | sim |
| `GET` | `/cidadaos/{id}` | Busca um cidadão | Nenhuma | `CidadaoResponseDTO` | `200` | `404` | sim |
| `POST` | `/cidadaos` | Cadastra um cidadão (barra e-mail repetido) | `CidadaoRequestDTO` | `CidadaoResponseDTO` | `201` | `400` | sim |
| `PUT` | `/cidadaos/{id}` | Atualiza um cidadão | `CidadaoRequestDTO` | `CidadaoResponseDTO` | `200` | `400`, `404` | não |
| `DELETE` | `/cidadaos/{id}` | Remove um cidadão | Nenhuma | Nenhuma (sem corpo) | `204` | `404` | não |

### Ocorrências

A visão do cidadão não traz a quantidade de reportes.

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `GET` | `/ocorrencias` | Lista todas as ocorrências | Nenhuma | lista de `OcorrenciaResponseDTO` | `200` | Nenhum | sim |
| `GET` | `/ocorrencias/{id}` | Busca uma ocorrência | Nenhuma | `OcorrenciaResponseDTO` | `200` | `404` | sim |
| `DELETE` | `/ocorrencias/{id}` | Remove uma ocorrência | Nenhuma | Nenhuma (sem corpo) | `204` | `404` | não |

### Rotas que não são CRUD

As que nascem das regras de negócio do README. Estas funcionam nesta etapa.

| Verbo | Caminho | O que faz | Regra | Sucesso | Erros |
|---|---|---|---|---|---|
| `POST` | `/ocorrencias` | Reporta um problema: encaminha ao órgão do tipo e, se já existe a mesma ocorrência no local, apenas soma 1 ao reporte | R1 e R2 | `201` | `400`, `404` |
| `PUT` | `/ocorrencias/{id}/status` | O órgão muda o status da ocorrência | R3 | `200` | `400`, `404` |
| `GET` | `/orgaos/{id}/ocorrencias` | Lista as ocorrências do órgão, com a contagem de reportes | Nenhuma | `200` | `404` |

## DTOs

O que entra não é igual ao que sai: o cliente não envia `id`, `status`, `orgaoId`, data nem contagem. Quem decide isso é a API.

| DTO | Direção | Campos | Validações |
|---|---|---|---|
| `OrgaoRequestDTO` | entrada | `nome`, `tipoAtendido` | os dois obrigatórios (`@NotBlank` e `@NotNull`) |
| `OrgaoResponseDTO` | saída | `id`, `nome`, `tipoAtendido`, `ativo` | Nenhuma |
| `CidadaoRequestDTO` | entrada | `nome`, `email` | `nome` obrigatório; `email` obrigatório e com formato válido (`@Email`) |
| `CidadaoResponseDTO` | saída | `id`, `nome`, `email` | Nenhuma |
| `OcorrenciaRequestDTO` | entrada | `cidadaoId`, `tipo`, `descricao`, `latitude`, `longitude` | todos obrigatórios; `latitude` entre -90 e 90; `longitude` entre -180 e 180 |
| `ConfirmacaoOcorrenciaDTO` | saída (do `POST /ocorrencias`) | `protocolo`, `mensagem` | Nenhuma |
| `OcorrenciaResponseDTO` | saída (visão do cidadão) | `id`, `tipo`, `descricao`, `latitude`, `longitude`, `status`, `dataAbertura`, `orgaoNome` | Nenhuma. Não tem contagem de reportes |
| `OcorrenciaOrgaoResponseDTO` | saída (visão do órgão) | os mesmos de `OcorrenciaResponseDTO`, mais `quantidadeReportes` | Nenhuma |
| `StatusRequestDTO` | entrada | `orgaoId`, `novoStatus` | os dois obrigatórios |

## Exemplos

Um par requisição/resposta por entidade.

### `POST /orgaos`

Requisição:

```json
{
  "nome": "Secretaria de Obras",
  "tipoAtendido": "BURACO"
}
```

Resposta, `201 Created`:

```json
{
  "id": 1,
  "nome": "Secretaria de Obras",
  "tipoAtendido": "BURACO",
  "ativo": true
}
```

### `POST /cidadaos`

Requisição:

```json
{
  "nome": "Maria Silva",
  "email": "maria@email.com"
}
```

Resposta, `201 Created`:

```json
{
  "id": 1,
  "nome": "Maria Silva",
  "email": "maria@email.com"
}
```

### `POST /ocorrencias`

Requisição:

```json
{
  "cidadaoId": 1,
  "tipo": "BURACO",
  "descricao": "Buraco grande na pista, perto do cruzamento",
  "latitude": -28.6775,
  "longitude": -49.3197
}
```

Resposta, `201 Created`. É a mesma quando a ocorrência é nova e quando o reporte foi agrupado em uma existente (R2): o cidadão não vê a diferença.

```json
{
  "protocolo": 1,
  "mensagem": "Ocorrência enviada com sucesso"
}
```

### `GET /ocorrencias/1` (visão do cidadão)

Resposta, `200 OK`, sem contagem:

```json
{
  "id": 1,
  "tipo": "BURACO",
  "descricao": "Buraco grande na pista, perto do cruzamento",
  "latitude": -28.6775,
  "longitude": -49.3197,
  "status": "ABERTA",
  "dataAbertura": "2026-10-26T19:30:00",
  "orgaoNome": "Secretaria de Obras"
}
```

### `GET /orgaos/1/ocorrencias` (visão do órgão)

Resposta, `200 OK`, com a contagem. Aqui duas pessoas reportaram o mesmo buraco:

```json
[
  {
    "id": 1,
    "tipo": "BURACO",
    "descricao": "Buraco grande na pista, perto do cruzamento",
    "latitude": -28.6775,
    "longitude": -49.3197,
    "status": "ABERTA",
    "dataAbertura": "2026-10-26T19:30:00",
    "orgaoNome": "Secretaria de Obras",
    "quantidadeReportes": 2
  }
]
```

### `PUT /ocorrencias/1/status`

Requisição:

```json
{
  "orgaoId": 1,
  "novoStatus": "EM_ATENDIMENTO"
}
```

Resposta, `200 OK` (visão do órgão, com contagem):

```json
{
  "id": 1,
  "tipo": "BURACO",
  "descricao": "Buraco grande na pista, perto do cruzamento",
  "latitude": -28.6775,
  "longitude": -49.3197,
  "status": "EM_ATENDIMENTO",
  "dataAbertura": "2026-10-26T19:30:00",
  "orgaoNome": "Secretaria de Obras",
  "quantidadeReportes": 2
}
```

## Erros

Todo erro sai no mesmo formato. Este é o `404` de `GET /ocorrencias/999`:

```json
{
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Ocorrência não encontrada: 999",
  "caminho": "/ocorrencias/999",
  "timestamp": "2026-10-26T19:30:00",
  "campos": []
}
```

No `400` de validação, a lista `campos` traz um item por campo inválido: `{"campo": "latitude", "mensagem": "Latitude deve estar entre -90 e 90"}`.

Este é o `400` do R3, quando outro órgão tenta mudar o status:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R3: somente o órgão responsável pela ocorrência pode alterar o status",
  "caminho": "/ocorrencias/1/status",
  "timestamp": "2026-10-26T19:30:00",
  "campos": []
}
```

| Status | Quando acontece neste projeto |
|---|---|
| `400` | JSON ilegível ou campo inválido no DTO de entrada |
| `404` | Id que não existe (ocorrência, órgão ou cidadão) |
| `400` | E-mail já cadastrado em `POST /cidadaos` |
| `400` | R1: nenhum órgão ativo atende o tipo da ocorrência |
| `400` | R3: o órgão que tenta mudar o status não é o responsável pela ocorrência |

O R2 não gera erro: ele aceita o pedido e agrupa o reporte.

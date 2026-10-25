// Define o pacote da camada de serviço.
package api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.dto.ConfirmacaoOcorrenciaDTO;
import api.dto.OcorrenciaOrgaoResponseDTO;
import api.dto.OcorrenciaRequestDTO;
import api.dto.OcorrenciaResponseDTO;
import api.dto.StatusRequestDTO;
import api.mapper.OcorrenciaMapper;
import api.model.Cidadao;
import api.model.Ocorrencia;
import api.model.Orgao;
import api.model.StatusOcorrencia;
import api.repository.CidadaoRepository;
import api.repository.OcorrenciaRepository;
import api.repository.OrgaoRepository;

// CAMADA: Service
// RESPONSABILIDADE: o coração do CidadeViva: recebe o reporte do cidadão,
//                   encaminha ao órgão certo, agrupa reportes repetidos e
//                   controla quem pode mudar o status.
// REGRAS: R1 (a ocorrência vai para o órgão ativo que atende o tipo; sem órgão, 400),
//         R2 (mesmo tipo, a menos de ~50 m e não resolvida: soma 1 no reporte em
//             vez de criar outra) e
//         R3 (só o órgão responsável muda o status; outro recebe 400).
// ROTAS ATENDIDAS: POST /ocorrencias (R1 e R2), PUT /ocorrencias/{id}/status (R3),
//                  GET /ocorrencias, GET /ocorrencias/{id} e GET /orgaos/{id}/ocorrencias
//                  (docs/contrato-da-api.md).
// SITUAÇÃO: implementada.
@Service
public class OcorrenciaService {

    // R2: "mesmo local" = dentro de 0,0005 grau de latitude e de longitude (cerca de 50 m).
    private static final double TOLERANCIA_GRAUS = 0.0005;

    private final OcorrenciaRepository repository;
    private final OrgaoRepository orgaoRepository;
    private final CidadaoRepository cidadaoRepository;
    private final OcorrenciaMapper mapper;

    public OcorrenciaService(OcorrenciaRepository repository, OrgaoRepository orgaoRepository,
                             CidadaoRepository cidadaoRepository, OcorrenciaMapper mapper) {
        this.repository = repository;
        this.orgaoRepository = orgaoRepository;
        this.cidadaoRepository = cidadaoRepository;
        this.mapper = mapper;
    }

    // Reporta um problema. Aplica R1 e R2 e devolve SEMPRE a mesma confirmação,
    // seja a ocorrência nova ou o reporte agrupado em uma existente.
    // @Transactional: ou grava tudo, ou nada (o +1 do R2 não fica pela metade).
    @Transactional
    public ConfirmacaoOcorrenciaDTO reportar(OcorrenciaRequestDTO dto) {
        // O cidadão precisa existir; senão, 404.
        Cidadao cidadao = cidadaoRepository.findById(dto.cidadaoId())
                .orElseThrow(() -> new CidadaoNaoEncontradoException(dto.cidadaoId()));

        // R1: acha o órgão ativo que atende o tipo. Se não existe, barra com 400.
        Orgao orgao = orgaoRepository.findFirstByTipoAtendidoAndAtivoTrueOrderByIdAsc(dto.tipo())
                .orElseThrow(() -> new RegraDeNegocioException(
                        "R1: não há órgão ativo que atenda o tipo " + dto.tipo()));

        // R2: já existe ocorrência do mesmo tipo, perto e não resolvida?
        double lat = dto.latitude();
        double lon = dto.longitude();
        Ocorrencia existente = repository
                .findFirstByTipoAndStatusNotAndLatitudeBetweenAndLongitudeBetweenOrderByIdAsc(
                        dto.tipo(), StatusOcorrencia.RESOLVIDA,
                        lat - TOLERANCIA_GRAUS, lat + TOLERANCIA_GRAUS,
                        lon - TOLERANCIA_GRAUS, lon + TOLERANCIA_GRAUS)
                .orElse(null);

        if (existente != null) {
            // Não cria outra: só soma mais um reporte na que já existe.
            existente.somarReporte();
            return mapper.toConfirmacao(repository.save(existente));
        }

        // Ocorrência nova, já ligada ao órgão encontrado pela R1.
        Ocorrencia nova = repository.save(mapper.toEntity(dto, orgao, cidadao));
        return mapper.toConfirmacao(nova);
    }

    // Lista todas as ocorrências na visão do cidadão (sem contagem de reportes).
    public List<OcorrenciaResponseDTO> listar() {
        return mapper.toResponseList(repository.findAll());
    }

    // Busca uma ocorrência na visão do cidadão; se não existir, 404.
    public OcorrenciaResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    // Lista as ocorrências de um órgão na visão do órgão (com a contagem de reportes).
    public List<OcorrenciaOrgaoResponseDTO> listarPorOrgao(Long orgaoId) {
        if (!orgaoRepository.existsById(orgaoId)) {
            throw new OrgaoNaoEncontradoException(orgaoId);
        }
        return mapper.toOrgaoResponseList(repository.findByOrgaoIdOrderByIdAsc(orgaoId));
    }

    // R3: só o órgão responsável pela ocorrência muda o status.
    @Transactional
    public OcorrenciaOrgaoResponseDTO mudarStatus(Long id, StatusRequestDTO dto) {
        Ocorrencia ocorrencia = buscarEntidade(id);

        // O órgão que pede precisa existir; senão, 404.
        if (!orgaoRepository.existsById(dto.orgaoId())) {
            throw new OrgaoNaoEncontradoException(dto.orgaoId());
        }

        // R3: existe, mas não é o responsável por esta ocorrência: 400.
        if (!ocorrencia.getOrgao().getId().equals(dto.orgaoId())) {
            throw new RegraDeNegocioException(
                    "R3: somente o órgão responsável pela ocorrência pode alterar o status");
        }

        ocorrencia.mudarStatus(dto.novoStatus());
        return mapper.toOrgaoResponse(repository.save(ocorrencia));
    }

    // Busca a entidade ou lança 404. Uso interno, por isso é private.
    private Ocorrencia buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() -> new OcorrenciaNaoEncontradaException(id));
    }
}

// Define o pacote da camada de serviço.
package api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import api.dto.OrgaoRequestDTO;
import api.dto.OrgaoResponseDTO;
import api.mapper.OrgaoMapper;
import api.model.Orgao;
import api.repository.OrgaoRepository;

// CAMADA: Service
// RESPONSABILIDADE: cadastro, listagem e busca de órgãos (a API simples do projeto).
// REGRAS: nenhuma. A R1 usa os órgãos, mas quem a aplica é o OcorrenciaService.
// ROTAS ATENDIDAS: POST /orgaos, GET /orgaos e GET /orgaos/{id}
//                  (docs/contrato-da-api.md, seção Órgãos).
// SITUAÇÃO: implementada.
@Service
public class OrgaoService {

    private final OrgaoRepository repository;
    private final OrgaoMapper mapper;

    public OrgaoService(OrgaoRepository repository, OrgaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // Cadastra um órgão: DTO vira entidade, grava e devolve o DTO de saída.
    public OrgaoResponseDTO criar(OrgaoRequestDTO dto) {
        Orgao salvo = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(salvo);
    }

    // Lista todos os órgãos.
    public List<OrgaoResponseDTO> listar() {
        return mapper.toResponseList(repository.findAll());
    }

    // Busca um órgão pelo id; se não existir, lança a exceção que vira 404.
    public OrgaoResponseDTO buscarPorId(Long id) {
        Orgao orgao = repository.findById(id).orElseThrow(() -> new OrgaoNaoEncontradoException(id));
        return mapper.toResponse(orgao);
    }
}

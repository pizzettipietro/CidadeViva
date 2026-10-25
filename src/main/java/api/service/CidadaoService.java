// Define o pacote da camada de serviço.
package api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import api.dto.CidadaoRequestDTO;
import api.dto.CidadaoResponseDTO;
import api.mapper.CidadaoMapper;
import api.model.Cidadao;
import api.repository.CidadaoRepository;

// CAMADA: Service
// RESPONSABILIDADE: cadastro, listagem e busca de cidadãos (a API simples do projeto).
//                   Barra e-mail repetido com 400, em vez de deixar o banco
//                   estourar um erro 500.
// REGRAS: nenhuma das R1 a R3 (e-mail único é integridade do cadastro).
// ROTAS ATENDIDAS: POST /cidadaos, GET /cidadaos e GET /cidadaos/{id}
//                  (docs/contrato-da-api.md, seção Cidadãos).
// SITUAÇÃO: implementada.
@Service
public class CidadaoService {

    private final CidadaoRepository repository;
    private final CidadaoMapper mapper;

    public CidadaoService(CidadaoRepository repository, CidadaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // Cadastra um cidadão, a não ser que o e-mail já esteja em uso.
    public CidadaoResponseDTO criar(CidadaoRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new RegraDeNegocioException("Já existe um cidadão cadastrado com este e-mail");
        }
        Cidadao salvo = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(salvo);
    }

    // Lista todos os cidadãos.
    public List<CidadaoResponseDTO> listar() {
        return mapper.toResponseList(repository.findAll());
    }

    // Busca um cidadão pelo id; se não existir, lança a exceção que vira 404.
    public CidadaoResponseDTO buscarPorId(Long id) {
        Cidadao cidadao = repository.findById(id).orElseThrow(() -> new CidadaoNaoEncontradoException(id));
        return mapper.toResponse(cidadao);
    }
}

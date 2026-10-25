// Define o pacote dos controllers.
package api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import api.dto.OcorrenciaOrgaoResponseDTO;
import api.dto.OrgaoRequestDTO;
import api.dto.OrgaoResponseDTO;
import api.service.OcorrenciaService;
import api.service.OrgaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// CAMADA: Controller
// RESPONSABILIDADE: porta de entrada dos órgãos. Só recebe a requisição, chama o
//                   Service e devolve a resposta: nenhuma regra de negócio aqui.
// ROTAS ATENDIDAS: POST /orgaos, GET /orgaos, GET /orgaos/{id} e
//                  GET /orgaos/{id}/ocorrencias (a visão do órgão, com a contagem de
//                  reportes). PUT e DELETE ficam para as próximas etapas.
// SITUAÇÃO: implementada.
@RestController
@RequestMapping("/orgaos")
@Tag(name = "Órgãos", description = "Secretarias e autoridades que recebem as ocorrências")
public class OrgaoController {

    private final OrgaoService service;
    private final OcorrenciaService ocorrenciaService;

    public OrgaoController(OrgaoService service, OcorrenciaService ocorrenciaService) {
        this.service = service;
        this.ocorrenciaService = ocorrenciaService;
    }

    @Operation(summary = "Cadastra um órgão")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrgaoResponseDTO criar(@Valid @RequestBody OrgaoRequestDTO dto) {
        return service.criar(dto);
    }

    @Operation(summary = "Lista os órgãos")
    @GetMapping
    public List<OrgaoResponseDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Busca um órgão pelo id")
    @GetMapping("/{id}")
    public OrgaoResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Lista as ocorrências do órgão, com a quantidade de reportes")
    @GetMapping("/{id}/ocorrencias")
    public List<OcorrenciaOrgaoResponseDTO> listarOcorrencias(@PathVariable Long id) {
        return ocorrenciaService.listarPorOrgao(id);
    }
}

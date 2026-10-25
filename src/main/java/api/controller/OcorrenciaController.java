// Define o pacote dos controllers.
package api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import api.dto.ConfirmacaoOcorrenciaDTO;
import api.dto.OcorrenciaOrgaoResponseDTO;
import api.dto.OcorrenciaRequestDTO;
import api.dto.OcorrenciaResponseDTO;
import api.dto.StatusRequestDTO;
import api.service.OcorrenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// CAMADA: Controller
// RESPONSABILIDADE: porta de entrada das ocorrências. Só recebe a requisição, chama o
//                   Service e devolve a resposta: as regras R1, R2 e R3 estão no
//                   OcorrenciaService, não aqui.
// ROTAS ATENDIDAS: POST /ocorrencias (R1 e R2), GET /ocorrencias, GET /ocorrencias/{id}
//                  e PUT /ocorrencias/{id}/status (R3). DELETE fica para as próximas etapas.
// SITUAÇÃO: implementada.
@RestController
@RequestMapping("/ocorrencias")
@Tag(name = "Ocorrências", description = "Problemas urbanos reportados pelos cidadãos")
public class OcorrenciaController {

    private final OcorrenciaService service;

    public OcorrenciaController(OcorrenciaService service) {
        this.service = service;
    }

    @Operation(summary = "Reporta um problema: encaminha ao órgão do tipo e agrupa reportes repetidos")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConfirmacaoOcorrenciaDTO reportar(@Valid @RequestBody OcorrenciaRequestDTO dto) {
        return service.reportar(dto);
    }

    @Operation(summary = "Lista todas as ocorrências (visão do cidadão)")
    @GetMapping
    public List<OcorrenciaResponseDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Busca uma ocorrência pelo id (visão do cidadão)")
    @GetMapping("/{id}")
    public OcorrenciaResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "O órgão responsável muda o status da ocorrência")
    @PutMapping("/{id}/status")
    public OcorrenciaOrgaoResponseDTO mudarStatus(@PathVariable Long id,
                                                  @Valid @RequestBody StatusRequestDTO dto) {
        return service.mudarStatus(id, dto);
    }
}

// Define o pacote da camada de conversão.
package api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import api.dto.ConfirmacaoOcorrenciaDTO;
import api.dto.OcorrenciaOrgaoResponseDTO;
import api.dto.OcorrenciaRequestDTO;
import api.dto.OcorrenciaResponseDTO;
import api.model.Cidadao;
import api.model.Ocorrencia;
import api.model.Orgao;

// CAMADA: Mapper
// RESPONSABILIDADE: converte entre os DTOs de ocorrência e a entidade Ocorrencia.
//                   Tem DUAS saídas: a do cidadão (sem quantidadeReportes) e a do
//                   órgão (com). Não consulta o banco e não tem regra de negócio:
//                   o órgão e o cidadão chegam prontos, buscados pelo Service.
// SITUAÇÃO: implementada.
@Component
public class OcorrenciaMapper {

    // Mensagem fixa da confirmação: igual para ocorrência nova e para reporte agrupado (R2).
    private static final String MENSAGEM_CONFIRMACAO = "Ocorrência enviada com sucesso";

    // DTO de entrada em entidade nova. O órgão (R1) e o cidadão vêm do Service.
    public Ocorrencia toEntity(OcorrenciaRequestDTO dto, Orgao orgao, Cidadao cidadao) {
        return new Ocorrencia(dto.tipo(), dto.descricao(), dto.latitude(), dto.longitude(), orgao, cidadao);
    }

    // Visão do cidadão: sem a contagem de reportes.
    public OcorrenciaResponseDTO toResponse(Ocorrencia o) {
        return new OcorrenciaResponseDTO(o.getId(), o.getTipo(), o.getDescricao(), o.getLatitude(),
                o.getLongitude(), o.getStatus(), o.getDataAbertura(), o.getOrgao().getNome());
    }

    // Lista na visão do cidadão.
    public List<OcorrenciaResponseDTO> toResponseList(List<Ocorrencia> ocorrencias) {
        return ocorrencias.stream().map(this::toResponse).toList();
    }

    // Visão do órgão: com a contagem de reportes.
    public OcorrenciaOrgaoResponseDTO toOrgaoResponse(Ocorrencia o) {
        return new OcorrenciaOrgaoResponseDTO(o.getId(), o.getTipo(), o.getDescricao(), o.getLatitude(),
                o.getLongitude(), o.getStatus(), o.getDataAbertura(), o.getOrgao().getNome(),
                o.getQuantidadeReportes());
    }

    // Lista na visão do órgão.
    public List<OcorrenciaOrgaoResponseDTO> toOrgaoResponseList(List<Ocorrencia> ocorrencias) {
        return ocorrencias.stream().map(this::toOrgaoResponse).toList();
    }

    // Confirmação devolvida ao cidadão: o protocolo é o id da ocorrência.
    public ConfirmacaoOcorrenciaDTO toConfirmacao(Ocorrencia o) {
        return new ConfirmacaoOcorrenciaDTO(o.getId(), MENSAGEM_CONFIRMACAO);
    }
}

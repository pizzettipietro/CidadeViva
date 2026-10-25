// Define o pacote da camada de acesso ao banco.
package api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.model.Ocorrencia;
import api.model.StatusOcorrencia;
import api.model.TipoOcorrencia;

// CAMADA: Repository
// RESPONSABILIDADE: lê e grava ocorrências no banco. Procura a ocorrência do
//                   mesmo tipo, ainda não resolvida, perto de um ponto (usado pela R2),
//                   e lista as ocorrências de um órgão.
// SITUAÇÃO: implementada.
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {

    // R2: tipo igual, status DIFERENTE do informado (o Service passa RESOLVIDA) e
    // latitude e longitude dentro das faixas. Devolve a mais antiga, se houver.
    Optional<Ocorrencia> findFirstByTipoAndStatusNotAndLatitudeBetweenAndLongitudeBetweenOrderByIdAsc(
            TipoOcorrencia tipo, StatusOcorrencia status,
            double latitudeMin, double latitudeMax,
            double longitudeMin, double longitudeMax);

    // Ocorrências de um órgão (orgao.id), da mais antiga para a mais nova.
    List<Ocorrencia> findByOrgaoIdOrderByIdAsc(Long orgaoId);
}

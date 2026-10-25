// Define o pacote das entidades do domínio.
package api.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: representa a tabela ocorrencias: um problema urbano em um
//                   local, com o andamento do atendimento e a quantidade de
//                   pessoas que o reportaram. Pertence a um órgão (R1) e foi
//                   aberta por um cidadão.
// SITUAÇÃO: implementada.
@Entity
@Table(name = "ocorrencias")
public class Ocorrencia {

    // O banco gera o id (BIGSERIAL na migration V3). Ele é o protocolo.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoOcorrencia tipo;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOcorrencia status;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "quantidade_reportes", nullable = false)
    private int quantidadeReportes;

    // Muitas ocorrências para um órgão: chave estrangeira orgao_id.
    @ManyToOne
    @JoinColumn(name = "orgao_id", nullable = false)
    private Orgao orgao;

    // Muitas ocorrências para um cidadão: chave estrangeira cidadao_id.
    @ManyToOne
    @JoinColumn(name = "cidadao_id", nullable = false)
    private Cidadao cidadao;

    // Exigido pelo Hibernate.
    protected Ocorrencia() {
    }

    // Uma ocorrência nova nasce ABERTA, com 1 reporte e a data de agora
    // (sem os nanossegundos, para o JSON sair limpo). O cliente não decide isso.
    public Ocorrencia(TipoOcorrencia tipo, String descricao, double latitude, double longitude,
                      Orgao orgao, Cidadao cidadao) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orgao = orgao;
        this.cidadao = cidadao;
        this.status = StatusOcorrencia.ABERTA;
        this.dataAbertura = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.quantidadeReportes = 1;
    }

    // Chamado pela R2: mais uma pessoa reportou o mesmo problema.
    public void somarReporte() {
        this.quantidadeReportes++;
    }

    // Chamado pela R3, depois de conferir que quem pede é o órgão responsável.
    public void mudarStatus(StatusOcorrencia novoStatus) {
        this.status = novoStatus;
    }

    public Long getId() {
        return id;
    }

    public TipoOcorrencia getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public int getQuantidadeReportes() {
        return quantidadeReportes;
    }

    public Orgao getOrgao() {
        return orgao;
    }

    public Cidadao getCidadao() {
        return cidadao;
    }
}

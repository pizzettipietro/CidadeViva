// Define o pacote das entidades do domínio.
package api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// CAMADA: Model
// RESPONSABILIDADE: representa a tabela cidadaos: a pessoa que reporta problemas.
// SITUAÇÃO: implementada.
@Entity
@Table(name = "cidadaos")
public class Cidadao {

    // O banco gera o id (BIGSERIAL na migration V2).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    // Exigido pelo Hibernate.
    protected Cidadao() {
    }

    public Cidadao(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}

package br.usp.projetoa.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.usp.projetoa.enums.SituacaoRelatorio;

@Entity
@Table(name = "pareceres")
public class Parecer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "relatorio_id", nullable = false)
    private Relatorio relatorio;

    @ManyToOne
    @JoinColumn(name = "numero_usp_parecista", referencedColumnName = "numeroUsp", nullable = false)
    private Docente parecista;

    @Column(nullable = false)
    private String parecer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoRelatorio desempenhoRelatorio;

    @Deprecated
    public Parecer() { }

    public Parecer(String parecer, SituacaoRelatorio desempenhoRelatorio) {
        this.parecer = parecer;
        this.desempenhoRelatorio = desempenhoRelatorio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Relatorio getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatorio relatorio) {
        this.relatorio = relatorio;
    }

    public Docente getParecista() {
        return parecista;
    }

    public void setParecista(Docente parecista) {
        this.parecista = parecista;
    }

    public String getParecer() {
        return parecer;
    }

    public void setParecer(String parecer) {
        this.parecer = parecer;
    }

    public SituacaoRelatorio getDesempenhoRelatorio() {
        return desempenhoRelatorio;
    }

    public void setDesempenhoRelatorio(SituacaoRelatorio desempenhoRelatorio) {
        this.desempenhoRelatorio = desempenhoRelatorio;
    }
}

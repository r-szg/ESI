package br.usp.projetoa.model;

import java.time.LocalDate;

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

import br.usp.projetoa.enums.SituacaoEntregaRelatorio;
import br.usp.projetoa.util.SemestreUtil;

@Entity
@Table(name = "entrega_relatorios")
public class StatusEntregaRelatorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "numero_usp_aluno", referencedColumnName = "numeroUsp", nullable = false)
    private Aluno aluno;

    @Column(nullable = false)
    private String semestreAno;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoEntregaRelatorio situacaoEntregaRelatorio;

    private LocalDate dataUltimoLembrete;

    private LocalDate prazoEntregaRelatorio;

    private Integer numeroDaEntrega;

    @Deprecated
    public StatusEntregaRelatorio() { }

    public StatusEntregaRelatorio(String semestreAno, SituacaoEntregaRelatorio situacaoEntregaRelatorio) {
        this.semestreAno = semestreAno;
        this.situacaoEntregaRelatorio = situacaoEntregaRelatorio;
        this.prazoEntregaRelatorio = SemestreUtil.getPrazoEntregaRelatorio();
        this.numeroDaEntrega = 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getSemestreAno() {
        return semestreAno;
    }

    public void setSemestreAno(String semestreAno) {
        this.semestreAno = semestreAno;
    }

    public SituacaoEntregaRelatorio getSituacaoEntregaRelatorio() {
        return situacaoEntregaRelatorio;
    }

    public void setSituacaoEntregaRelatorio(SituacaoEntregaRelatorio situacaoEntregaRelatorio) {
        this.situacaoEntregaRelatorio = situacaoEntregaRelatorio;
    }

    public LocalDate getDataUltimoLembrete() {
        return dataUltimoLembrete;
    }

    public void setDataUltimoLembrete(LocalDate dataUltimoLembrete) {
        this.dataUltimoLembrete = dataUltimoLembrete;
    }

    public LocalDate getPrazoEntregaRelatorio() {
        return prazoEntregaRelatorio;
    }

    public void setPrazoEntregaRelatorio(LocalDate prazoEntregaRelatorio) {
        this.prazoEntregaRelatorio = prazoEntregaRelatorio;
    }

    public Integer getNumeroDaEntrega() {
        return numeroDaEntrega;
    }

    public void setNumeroDaEntrega(Integer numeroDaEntrega) {
        this.numeroDaEntrega = numeroDaEntrega;
    }
}

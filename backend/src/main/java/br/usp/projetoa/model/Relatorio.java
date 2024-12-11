package br.usp.projetoa.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import br.usp.projetoa.enums.Quantidade;
import br.usp.projetoa.enums.SituacaoExame;
import br.usp.projetoa.enums.SituacaoRelatorio;
import br.usp.projetoa.enums.TipoCurso;

@Entity
@Table(name = "relatorios")
public class Relatorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "numero_usp_aluno", referencedColumnName = "numeroUsp", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "numero_usp_docente", referencedColumnName = "numeroUsp", nullable = false)
    private Docente docente;
    
    @Column(nullable = false)
    private LocalDate dataUltimaAtualizacaoLattes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoCurso tipoCurso;

    @Column(nullable = false)
    private LocalDate mesAnoIngressoComoAlunoRegular;

    @Column(nullable = false)
    private SituacaoRelatorio resultadoUltimoRelatorio;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Quantidade quantidadeAprovacoesDesdeInicioCurso;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Quantidade quantidadeReprovacoes2023S2;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Quantidade quantidadeReprovacoesDesdeInicioCurso;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoExame realizouExameProficienciaLinguas;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoExame realizouExameQualificacao;

    private LocalDate prazoMaximoInscricaoExameQualificacao;
    
    @Column(nullable = false)
    private LocalDate prazoMaximoDepositoDissertacao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Quantidade artigosEmFaseEscrita;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Quantidade artigosSubmetidosEEmPeriodoAvaliacao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Quantidade artigosAceitosOuPublicados;

    @Column(nullable = false)
    private String participacaoAtividadesAcademicas2024S1;

    @Column(nullable = false)
    private String resumoAtividadesPesquisa;

    private String declaracaoAdicionalParaCCP;
    
    private Boolean necessidadeApoioCoordenacao;

    @OneToMany(mappedBy = "relatorio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Parecer> pareceres;

    @Deprecated
    public Relatorio() {}

    public Relatorio(LocalDate dataUltimaAtualizacaoLattes, TipoCurso tipoCurso, 
            LocalDate mesAnoIngressoComoAlunoRegular, SituacaoRelatorio resultadoUltimoRelatorio,
            Quantidade quantidadeAprovacoesDesdeInicioCurso, Quantidade quantidadeReprovacoes2023S2,
            Quantidade quantidadeReprovacoesDesdeInicioCurso, SituacaoExame realizouExameProficienciaLinguas,
            SituacaoExame realizouExameQualificacao, LocalDate prazoMaximoInscricaoExameQualificacao,
            LocalDate prazoMaximoDepositoDissertacao, Quantidade artigosEmFaseEscrita,
            Quantidade artigosSubmetidosEEmPeriodoAvaliacao, Quantidade artigosAceitosOuPublicados,
            String participacaoAtividadesAcademicas2024S1, String resumoAtividadesPesquisa,
            String declaracaoAdicionalParaCCP, Boolean necessidadeApoioCoordenacao) {
        this.dataUltimaAtualizacaoLattes = dataUltimaAtualizacaoLattes;
        this.tipoCurso = tipoCurso;
        this.mesAnoIngressoComoAlunoRegular = mesAnoIngressoComoAlunoRegular;
        this.resultadoUltimoRelatorio = resultadoUltimoRelatorio;
        this.quantidadeAprovacoesDesdeInicioCurso = quantidadeAprovacoesDesdeInicioCurso;
        this.quantidadeReprovacoes2023S2 = quantidadeReprovacoes2023S2;
        this.quantidadeReprovacoesDesdeInicioCurso = quantidadeReprovacoesDesdeInicioCurso;
        this.realizouExameProficienciaLinguas = realizouExameProficienciaLinguas;
        this.realizouExameQualificacao = realizouExameQualificacao;
        this.prazoMaximoInscricaoExameQualificacao = prazoMaximoInscricaoExameQualificacao;
        this.prazoMaximoDepositoDissertacao = prazoMaximoDepositoDissertacao;
        this.artigosEmFaseEscrita = artigosEmFaseEscrita;
        this.artigosSubmetidosEEmPeriodoAvaliacao = artigosSubmetidosEEmPeriodoAvaliacao;
        this.artigosAceitosOuPublicados = artigosAceitosOuPublicados;
        this.participacaoAtividadesAcademicas2024S1 = participacaoAtividadesAcademicas2024S1;
        this.resumoAtividadesPesquisa = resumoAtividadesPesquisa;
        this.declaracaoAdicionalParaCCP = declaracaoAdicionalParaCCP;
        this.necessidadeApoioCoordenacao = necessidadeApoioCoordenacao;
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

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public LocalDate getDataUltimaAtualizacaoLattes() {
        return dataUltimaAtualizacaoLattes;
    }

    public void setDataUltimaAtualizacaoLattes(LocalDate dataUltimaAtualizacaoLattes) {
        this.dataUltimaAtualizacaoLattes = dataUltimaAtualizacaoLattes;
    }

    public TipoCurso getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(TipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public LocalDate getMesAnoIngressoComoAlunoRegular() {
        return mesAnoIngressoComoAlunoRegular;
    }

    public void setMesAnoIngressoComoAlunoRegular(LocalDate mesAnoIngressoComoAlunoRegular) {
        this.mesAnoIngressoComoAlunoRegular = mesAnoIngressoComoAlunoRegular;
    }

    public SituacaoRelatorio getResultadoUltimoRelatorio() {
        return resultadoUltimoRelatorio;
    }

    public void setResultadoUltimoRelatorio(SituacaoRelatorio resultadoUltimoRelatorio) {
        this.resultadoUltimoRelatorio = resultadoUltimoRelatorio;
    }

    public Quantidade getQuantidadeAprovacoesDesdeInicioCurso() {
        return quantidadeAprovacoesDesdeInicioCurso;
    }

    public void setQuantidadeAprovacoesDesdeInicioCurso(Quantidade quantidadeAprovacoesDesdeInicioCurso) {
        this.quantidadeAprovacoesDesdeInicioCurso = quantidadeAprovacoesDesdeInicioCurso;
    }

    public Quantidade getQuantidadeReprovacoes2023S2() {
        return quantidadeReprovacoes2023S2;
    }

    public void setQuantidadeReprovacoes2023S2(Quantidade quantidadeReprovacoes2023S2) {
        this.quantidadeReprovacoes2023S2 = quantidadeReprovacoes2023S2;
    }

    public Quantidade getQuantidadeReprovacoesDesdeInicioCurso() {
        return quantidadeReprovacoesDesdeInicioCurso;
    }

    public void setQuantidadeReprovacoesDesdeInicioCurso(Quantidade quantidadeReprovacoesDesdeInicioCurso) {
        this.quantidadeReprovacoesDesdeInicioCurso = quantidadeReprovacoesDesdeInicioCurso;
    }

    public SituacaoExame getRealizouExameProficienciaLinguas() {
        return realizouExameProficienciaLinguas;
    }

    public void setRealizouExameProficienciaLinguas(SituacaoExame realizouExameProficienciaLinguas) {
        this.realizouExameProficienciaLinguas = realizouExameProficienciaLinguas;
    }

    public SituacaoExame getRealizouExameQualificacao() {
        return realizouExameQualificacao;
    }

    public void setRealizouExameQualificacao(SituacaoExame realizouExameQualificacao) {
        this.realizouExameQualificacao = realizouExameQualificacao;
    }

    public LocalDate getPrazoMaximoInscricaoExameQualificacao() {
        return prazoMaximoInscricaoExameQualificacao;
    }

    public void setPrazoMaximoInscricaoExameQualificacao(LocalDate prazoMaximoInscricaoExameQualificacao) {
        this.prazoMaximoInscricaoExameQualificacao = prazoMaximoInscricaoExameQualificacao;
    }

    public LocalDate getPrazoMaximoDepositoDissertacao() {
        return prazoMaximoDepositoDissertacao;
    }

    public void setPrazoMaximoDepositoDissertacao(LocalDate prazoMaximoDepositoDissertacao) {
        this.prazoMaximoDepositoDissertacao = prazoMaximoDepositoDissertacao;
    }

    public Quantidade getArtigosEmFaseEscrita() {
        return artigosEmFaseEscrita;
    }

    public void setArtigosEmFaseEscrita(Quantidade artigosEmFaseEscrita) {
        this.artigosEmFaseEscrita = artigosEmFaseEscrita;
    }

    public Quantidade getArtigosSubmetidosEEmPeriodoAvaliacao() {
        return artigosSubmetidosEEmPeriodoAvaliacao;
    }

    public void setArtigosSubmetidosEEmPeriodoAvaliacao(Quantidade artigosSubmetidosEEmPeriodoAvaliacao) {
        this.artigosSubmetidosEEmPeriodoAvaliacao = artigosSubmetidosEEmPeriodoAvaliacao;
    }

    public Quantidade getArtigosAceitosOuPublicados() {
        return artigosAceitosOuPublicados;
    }

    public void setArtigosAceitosOuPublicados(Quantidade artigosAceitosOuPublicados) {
        this.artigosAceitosOuPublicados = artigosAceitosOuPublicados;
    }

    public String getParticipacaoAtividadesAcademicas2024S1() {
        return participacaoAtividadesAcademicas2024S1;
    }

    public void setParticipacaoAtividadesAcademicas2024S1(String participacaoAtividadesAcademicas2024S1) {
        this.participacaoAtividadesAcademicas2024S1 = participacaoAtividadesAcademicas2024S1;
    }

    public String getResumoAtividadesPesquisa() {
        return resumoAtividadesPesquisa;
    }

    public void setResumoAtividadesPesquisa(String resumoAtividadesPesquisa) {
        this.resumoAtividadesPesquisa = resumoAtividadesPesquisa;
    }

    public String getDeclaracaoAdicionalParaCCP() {
        return declaracaoAdicionalParaCCP;
    }

    public void setDeclaracaoAdicionalParaCCP(String declaracaoAdicionalParaCCP) {
        this.declaracaoAdicionalParaCCP = declaracaoAdicionalParaCCP;
    }

    public Boolean isNecessidadeApoioCoordenacao() {
        return necessidadeApoioCoordenacao;
    }

    public void setNecessidadeApoioCoordenacao(Boolean necessidadeApoioCoordenacao) {
        this.necessidadeApoioCoordenacao = necessidadeApoioCoordenacao;
    }

    public List<Parecer> getPareceres() {
        return pareceres;
    }

    public void setPareceres(List<Parecer> pareceres) {
        this.pareceres = pareceres;
    }
}

package br.usp.projetoa.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.usp.projetoa.enums.TipoCurso;

@Entity
@Table(name = "alunos")
public class Aluno implements UsuarioAutenticavel {
    @Id
    private Long numeroUsp;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private String rg;

    @Column(nullable = false)
    private String localNascimento;

    @Column(nullable = false)
    private String nacionalidade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoCurso tipoCurso;

    @Column(nullable = false)
    private String linkLattes;

    @Column(nullable = false)
    private LocalDate dataMatricula;
    
    @Column(nullable = false)
    private LocalDate dataAprovacaoExameQualificacao;
    
    @Column(nullable = false)
    private LocalDate dataAprovacaoExameProficienciaLinguas;
    
    @Column(nullable = false)
    private LocalDate dataLimiteDepositoTrabalhoFinal;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matricula> disciplinas = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Relatorio> relatorios;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatusEntregaRelatorio> statusEntregaRelatorios;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "roles_aluno",
        joinColumns = @JoinColumn(name = "numero_usp"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Override
    public String getUsername() {
        return numeroUsp.toString();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public Set<Role> getRoles() {
        return roles;
    }

    @Deprecated
    public Aluno() { }

    public Aluno(Long numeroUsp, String nomeCompleto, String email, String senha, LocalDate dataNascimento, String rg,
            String localNascimento, String nacionalidade, TipoCurso tipoCurso, String linkLattes,
            LocalDate dataMatricula, LocalDate dataAprovacaoExameQualificacao,
            LocalDate dataAprovacaoExameProficienciaLinguas, LocalDate dataLimiteDepositoTrabalhoFinal) {
        this.numeroUsp = numeroUsp;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.rg = rg;
        this.localNascimento = localNascimento;
        this.nacionalidade = nacionalidade;
        this.tipoCurso = tipoCurso;
        this.linkLattes = linkLattes;
        this.dataMatricula = dataMatricula;
        this.dataAprovacaoExameQualificacao = dataAprovacaoExameQualificacao;
        this.dataAprovacaoExameProficienciaLinguas = dataAprovacaoExameProficienciaLinguas;
        this.dataLimiteDepositoTrabalhoFinal = dataLimiteDepositoTrabalhoFinal;
    }

    public Long getNumeroUsp() {
        return numeroUsp;
    }

    public void setNumeroUsp(Long numeroUsp) {
        this.numeroUsp = numeroUsp;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getLocalNascimento() {
        return localNascimento;
    }

    public void setLocalNascimento(String localNascimento) {
        this.localNascimento = localNascimento;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public TipoCurso getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(TipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public String getLinkLattes() {
        return linkLattes;
    }

    public void setLinkLattes(String linkLattes) {
        this.linkLattes = linkLattes;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }
    
    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }
    
    public LocalDate getDataAprovacaoExameQualificacao() {
        return dataAprovacaoExameQualificacao;
    }
    
    public void setDataAprovacaoExameQualificacao(LocalDate dataAprovacaoExameQualificacao) {
        this.dataAprovacaoExameQualificacao = dataAprovacaoExameQualificacao;
    }
    
    public LocalDate getDataAprovacaoExameProficienciaLinguas() {
        return dataAprovacaoExameProficienciaLinguas;
    }
    
    public void setDataAprovacaoExameProficienciaLinguas(LocalDate dataAprovacaoExameProficienciaLinguas) {
        this.dataAprovacaoExameProficienciaLinguas = dataAprovacaoExameProficienciaLinguas;
    }
    
    public LocalDate getDataLimiteDepositoTrabalhoFinal() {
        return dataLimiteDepositoTrabalhoFinal;
    }
    
    public void setDataLimiteDepositoTrabalhoFinal(LocalDate dataLimiteDepositoTrabalhoFinal) {
        this.dataLimiteDepositoTrabalhoFinal = dataLimiteDepositoTrabalhoFinal;
    }

    public List<Matricula> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Matricula> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public Set<Relatorio> getRelatorios() {
        return relatorios;
    }

    public void setRelatorios(Set<Relatorio> relatorios) {
        this.relatorios = relatorios;
    }

    public List<StatusEntregaRelatorio> getStatusEntregaRelatorios() {
        return statusEntregaRelatorios;
    }

    public void setStatusEntregaRelatorios(List<StatusEntregaRelatorio> statusEntregaRelatorios) {
        this.statusEntregaRelatorios = statusEntregaRelatorios;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}

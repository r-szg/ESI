package br.usp.projetoa.model;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "docentes")
public class Docente implements UsuarioAutenticavel {
    @Id
    private Long numeroUsp;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean ehOrientador;

    @Column(nullable = false)
    private Boolean ehMembroCCP;

    @JsonIgnore
    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Relatorio> relatoriosOrientandos;

    @JsonIgnore
    @OneToMany(mappedBy = "parecista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parecer> pareceres;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "roles_docente",
        joinColumns = @JoinColumn(name = "numero_usp"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

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
    public Docente() {}

    public Docente(Long numeroUsp, String nomeCompleto, String senha, String email, 
        Boolean ehOrientador, Boolean ehMembroCCP) {
        this.numeroUsp = numeroUsp;
        this.nomeCompleto = nomeCompleto;
        this.senha = senha;
        this.email = email;
        this.ehOrientador = ehOrientador;
        this.ehMembroCCP = ehMembroCCP;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Boolean getEhOrientador() {
        return ehOrientador;
    }

    public void setEhOrientador(Boolean ehOrientador) {
        this.ehOrientador = ehOrientador;
    }

    public Boolean getEhMembroCCP() {
        return ehMembroCCP;
    }

    public void setEhMembroCCP(Boolean ehMembroCCP) {
        this.ehMembroCCP = ehMembroCCP;
    }

    public List<Relatorio> getRelatoriosOrientandos() {
        return relatoriosOrientandos;
    }

    public void setRelatoriosOrientandos(List<Relatorio> relatoriosOrientandos) {
        this.relatoriosOrientandos = relatoriosOrientandos;
    }

    public List<Parecer> getPareceres() {
        return pareceres;
    }

    public void setPareceres(List<Parecer> pareceres) {
        this.pareceres = pareceres;
    }
}

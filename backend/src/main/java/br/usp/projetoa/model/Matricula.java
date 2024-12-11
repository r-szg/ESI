package br.usp.projetoa.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import br.usp.projetoa.enums.SituacaoDisciplina;

@Entity
@Table(name = "matriculas")
public class Matricula {

    @EmbeddedId
    private MatriculaId id;

    @ManyToOne
    @MapsId("alunoId")
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @MapsId("disciplinaId")
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;

    @Column(nullable = false)
    private SituacaoDisciplina situacaoDisciplina;

    @Deprecated
    public Matricula() { }

    public Matricula(Aluno aluno, Disciplina disciplina, SituacaoDisciplina situacaoDisciplina) {
        this.id = new MatriculaId(aluno.getNumeroUsp(), disciplina.getCodigoDisciplina());
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.situacaoDisciplina = situacaoDisciplina;
    }

    public MatriculaId getId() {
        return id;
    }

    public void setId(MatriculaId id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public SituacaoDisciplina getSituacaoDisciplina() {
        return situacaoDisciplina;
    }

    public void setSituacaoDisciplina(SituacaoDisciplina situacaoDisciplina) {
        this.situacaoDisciplina = situacaoDisciplina;
    }
}

package br.usp.projetoa.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class MatriculaId implements Serializable {
    private Long alunoId;
    private String disciplinaId;

    @Deprecated
    public MatriculaId() { }

    public MatriculaId(Long alunoId, String disciplinaId) {
        this.alunoId = alunoId;
        this.disciplinaId = disciplinaId;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public String getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(String disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatriculaId that = (MatriculaId) o;
        return Objects.equals(alunoId, that.alunoId) &&
               Objects.equals(disciplinaId, that.disciplinaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alunoId, disciplinaId);
    }
}

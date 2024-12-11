package br.usp.projetoa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.usp.projetoa.dto.MatriculaDto;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.Disciplina;
import br.usp.projetoa.model.Matricula;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DisciplinaRepository;
import br.usp.projetoa.repository.MatriculaRepository;

@Service
public class MatriculaService {
    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;

    public MatriculaService(MatriculaRepository matriculaRepository, AlunoRepository alunoRepository,
            DisciplinaRepository disciplinaRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<Matricula> buscarTodasMatriculas() {
        return matriculaRepository.findAll();
    }

    public Matricula matricularAluno(MatriculaDto matriculaDto) {
        Aluno aluno = alunoRepository.findById(matriculaDto.numeroUsp()).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Disciplina disciplina = disciplinaRepository.findById(matriculaDto.codigoDisciplina()).orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        return matriculaRepository.save(new Matricula(aluno, disciplina, matriculaDto.situacaoDisciplina()));
    }

    public List<Matricula> buscarMatriculasPorAluno(Long numeroUsp) {
        List<Matricula> matriculas = matriculaRepository.findByAluno_NumeroUsp(numeroUsp).stream()
            .map(
                matricula ->
                    new Matricula(
                        matricula.getAluno(),
                        matricula.getDisciplina(),
                        matricula.getSituacaoDisciplina()
                    )
            )
            .collect(Collectors.toList());

        return matriculas;
    }
}

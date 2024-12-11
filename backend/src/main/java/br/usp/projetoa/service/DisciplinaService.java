package br.usp.projetoa.service;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.stereotype.Service;

import br.usp.projetoa.dto.DisciplinaDto;
import br.usp.projetoa.exception.EntidadeDuplicadaException;
import br.usp.projetoa.model.Disciplina;
import br.usp.projetoa.repository.DisciplinaRepository;

@Service
public class DisciplinaService {
    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<Disciplina> buscarTodasDisciplinas() {
        return disciplinaRepository.findAll();
    }

    public Optional<Disciplina> buscarDisciplina(String codigoDisciplina) {
        return disciplinaRepository.findById(codigoDisciplina);
    }

    public String criarDisciplina(DisciplinaDto disciplinaDto) {
        if (disciplinaRepository.existsById(disciplinaDto.codigoDisciplina())) {
            throw new EntidadeDuplicadaException("Disciplina", disciplinaDto.codigoDisciplina());
        }

        Disciplina disciplina = new Disciplina(disciplinaDto.codigoDisciplina(), disciplinaDto.nomeDisciplina());

        return disciplinaRepository.save(disciplina).getCodigoDisciplina();
    }

    public Disciplina atualizarDisciplina(String codigoDisciplina, @Valid DisciplinaDto disciplinaDto) {
        Optional<Disciplina> disciplinaOptional = disciplinaRepository.findById(codigoDisciplina);

        if (disciplinaOptional.isEmpty()) return null;

        Disciplina disciplina = disciplinaOptional.get();
        disciplina.setCodigoDisciplina(disciplinaDto.codigoDisciplina());
        disciplina.setNomeDisciplina(disciplinaDto.nomeDisciplina());

        return disciplinaRepository.save(disciplina);
    }

    @Transactional
    public boolean deletarDisciplina(String codigoDisciplina) {
        if (!disciplinaRepository.existsById(codigoDisciplina)) return false;

        disciplinaRepository.deleteById(codigoDisciplina);
        return true;
    }
}

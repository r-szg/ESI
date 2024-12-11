package br.usp.projetoa.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.usp.projetoa.dto.AlunoDto;
import br.usp.projetoa.enums.RoleEnum;
import br.usp.projetoa.enums.SituacaoEntregaRelatorio;
import br.usp.projetoa.exception.EntidadeDuplicadaException;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.Role;
import br.usp.projetoa.model.StatusEntregaRelatorio;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DocenteRepository;
import br.usp.projetoa.repository.RoleRepository;
import br.usp.projetoa.repository.StatusEntregaRelatorioRepository;
import br.usp.projetoa.util.SemestreUtil;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final DocenteRepository docenteRepository;
    private final RoleRepository roleRepository;
    private final StatusEntregaRelatorioRepository statusEntregaRelatorioRepository;

    private final PasswordEncoder passwordEncoder;

    public AlunoService(AlunoRepository alunoRepository, DocenteRepository docenteRepository, 
        PasswordEncoder passwordEncoder, RoleRepository roleRepository, 
        StatusEntregaRelatorioRepository statusEntregaRelatorioRepository) {
        this.alunoRepository = alunoRepository;
        this.docenteRepository = docenteRepository;
        this.roleRepository = roleRepository;
        this.statusEntregaRelatorioRepository = statusEntregaRelatorioRepository;
        this.passwordEncoder= passwordEncoder;
    }

    public List<Aluno> buscarTodosAlunos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorNumeroUsp(Long numeroUsp) {
        return alunoRepository.findById(numeroUsp);
    }

    public Long criarAluno(AlunoDto alunoDto) {
        if (alunoRepository.existsById(alunoDto.numeroUsp()) 
            || docenteRepository.existsById(alunoDto.numeroUsp())) {
            throw new EntidadeDuplicadaException("Aluno", Long.toString(alunoDto.numeroUsp()));
        }

        Aluno aluno = new Aluno(
            alunoDto.numeroUsp(),
            alunoDto.nomeCompleto(),
            alunoDto.email(),
            passwordEncoder.encode(alunoDto.senha()),
            alunoDto.dataNascimento(),
            alunoDto.rg(),
            alunoDto.localNascimento(),
            alunoDto.nacionalidade(),
            alunoDto.tipoCurso(),
            alunoDto.linkLattes(),
            alunoDto.dataMatricula(),
            alunoDto.dataAprovacaoExameQualificacao(),
            alunoDto.dataAprovacaoExameProficienciaLinguas(),
            alunoDto.dataLimiteDepositoTrabalhoFinal()
        );

        Optional<Role> roleOptional = roleRepository.findByRole(RoleEnum.ROLE_ALUNO);

        Role role;
        if (roleOptional.isPresent()) {
            role = roleOptional.get();
        } else {
            role = new Role(RoleEnum.ROLE_ALUNO);
            roleRepository.save(role);
        }

        aluno.setRoles(Set.of(role));
        
        Aluno alunoSalvo = alunoRepository.save(aluno);

        // Define a entrega do relatório como pendente para o aluno que será criado
        StatusEntregaRelatorio statusEntregaRelatorio = new StatusEntregaRelatorio(
                SemestreUtil.determinaSemestreAnoAtual(), 
                SituacaoEntregaRelatorio.PENDENTE);

        statusEntregaRelatorio.setAluno(alunoSalvo);
        StatusEntregaRelatorio statusEntregaRelatorioSalvo = statusEntregaRelatorioRepository.save(statusEntregaRelatorio);

        alunoSalvo.setStatusEntregaRelatorios(new ArrayList<>(Arrays.asList(statusEntregaRelatorioSalvo)));

        return alunoRepository.save(alunoSalvo).getNumeroUsp();
    }

    public Aluno atualizarAluno(Long numeroUsp, AlunoDto alunoDto) {
        Optional<Aluno> alunoOptional = alunoRepository.findById(numeroUsp);

        if (alunoOptional.isEmpty()) return null;

        Aluno aluno = alunoOptional.get();
        aluno.setNumeroUsp(numeroUsp);
        aluno.setNomeCompleto(alunoDto.nomeCompleto());
        aluno.setEmail(alunoDto.email());
        aluno.setDataNascimento(alunoDto.dataNascimento());
        aluno.setRg(alunoDto.rg());
        aluno.setLocalNascimento(alunoDto.localNascimento());
        aluno.setNacionalidade(alunoDto.nacionalidade());
        aluno.setTipoCurso(alunoDto.tipoCurso());
        aluno.setLinkLattes(alunoDto.linkLattes());
        aluno.setDataMatricula(alunoDto.dataMatricula());
        aluno.setDataAprovacaoExameQualificacao(alunoDto.dataAprovacaoExameQualificacao());
        aluno.setDataAprovacaoExameProficienciaLinguas(alunoDto.dataAprovacaoExameProficienciaLinguas());
        aluno.setDataLimiteDepositoTrabalhoFinal(alunoDto.dataLimiteDepositoTrabalhoFinal());

        return alunoRepository.save(aluno);
    }

    @Transactional
    public boolean deletarAluno(Long numeroUsp) {
        if (!alunoRepository.existsById(numeroUsp)) return false;

        alunoRepository.deleteById(numeroUsp);
        return true;
    }
}

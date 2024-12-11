package br.usp.projetoa.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.usp.projetoa.dto.DocenteDto;
import br.usp.projetoa.enums.RoleEnum;
import br.usp.projetoa.exception.EntidadeDuplicadaException;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.model.Role;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DocenteRepository;
import br.usp.projetoa.repository.RoleRepository;

@Service
public class DocenteService {
    private final DocenteRepository docenteRepository;
    private final AlunoRepository alunoRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public DocenteService(DocenteRepository docenteRepository, AlunoRepository alunoRepository, 
        RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.docenteRepository = docenteRepository;
        this.alunoRepository = alunoRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Docente> buscarTodosDocentes() {
        return docenteRepository.findAll();
    }

    public Optional<Docente> buscarPorNumeroUsp(Long numeroUsp) {
        return docenteRepository.findById(numeroUsp);
    }

    public Long criarDocente(DocenteDto docenteDto) {
        if (docenteRepository.existsById(docenteDto.numeroUsp())
            || alunoRepository.existsById(docenteDto.numeroUsp())) {
            throw new EntidadeDuplicadaException("Docente", Long.toString(docenteDto.numeroUsp()));
        }

        Docente docente = new Docente(
            docenteDto.numeroUsp(), 
            docenteDto.nomeCompleto(), 
            passwordEncoder.encode(docenteDto.senha()),
            docenteDto.email(),
            docenteDto.ehOrientador(),
            docenteDto.ehMembroCCP()
        );

        Set<Role> roles = new HashSet<>();


        Role role;
        if (docenteDto.ehOrientador()) {
            Optional<Role> roleOptional = roleRepository.findByRole(RoleEnum.ROLE_ORIENTADOR);
            if (roleOptional.isPresent()) {
                role = roleOptional.get();
            } else {
                role = new Role(RoleEnum.ROLE_ORIENTADOR);
                roleRepository.save(role);
            }

            roles.add(role);
        }

        if (docenteDto.ehMembroCCP()) {
            Optional<Role> roleOptional = roleRepository.findByRole(RoleEnum.ROLE_CCP);
            if (roleOptional.isPresent()) {
                role = roleOptional.get();
            } else {
                role = new Role(RoleEnum.ROLE_CCP);
                roleRepository.save(role);
            }

            roles.add(role);
        }

        docente.setRoles(roles);

        return docenteRepository.save(docente).getNumeroUsp();
    }

    public Docente atualizarDocente(Long numeroUsp, DocenteDto docenteDto) {
        Optional<Docente> docenteOptional = docenteRepository.findById(numeroUsp);

        if (docenteOptional.isEmpty()) return null;

        Docente docente = docenteOptional.get();
        docente.setNumeroUsp(numeroUsp);
        docente.setNomeCompleto(docenteDto.nomeCompleto());
        docente.setEmail(docenteDto.email());

        return docenteRepository.save(docente);
    }

    @Transactional
    public boolean deletarDocente(Long numeroUsp) {
        if (!docenteRepository.existsById(numeroUsp)) return false;

        docenteRepository.deleteById(numeroUsp);
        return true;
    }
}

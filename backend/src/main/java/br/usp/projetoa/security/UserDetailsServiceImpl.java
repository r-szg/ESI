package br.usp.projetoa.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DocenteRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AlunoRepository alunoRepository;
    private final DocenteRepository docenteRepository;
    
    public UserDetailsServiceImpl(AlunoRepository alunoRepository, DocenteRepository docenteRepository) {
        this.alunoRepository = alunoRepository;
        this.docenteRepository = docenteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Aluno> alunoOptional = alunoRepository.findById(Long.parseLong(username));
        if (alunoOptional.isPresent()) return new UserDetailsImpl(alunoOptional.get());
        
        Optional<Docente> docenteOptional = docenteRepository.findById(Long.parseLong(username));
        if (docenteOptional.isPresent()) return new UserDetailsImpl(docenteOptional.get());

        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}

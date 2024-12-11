package br.usp.projetoa.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.model.UsuarioAutenticavel;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DocenteRepository;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;
    private final AlunoRepository alunoRepository;
    private final DocenteRepository docenteRepository;

    public UserAuthenticationFilter(JwtTokenService jwtTokenService, AlunoRepository alunoRepository, 
        DocenteRepository docenteRepository) {
        this.jwtTokenService = jwtTokenService;
        this.alunoRepository = alunoRepository;
        this.docenteRepository = docenteRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!verificaSeEndpointEPublico(request)) {
            String token = recuperaToken(request);
            if (token == null) throw new RuntimeException("Token ausente.");

            String subject = jwtTokenService.extraiSubjectDoToken(token);
            UsuarioAutenticavel usuario = null;

            Optional<Aluno> alunoOptional = alunoRepository.findById(Long.parseLong(subject));
            if (alunoOptional.isPresent()) usuario = alunoOptional.get();

            Optional<Docente> docenteOptional = docenteRepository.findById(Long.parseLong(subject));
            if (docenteOptional.isPresent()) usuario = docenteOptional.get();

            UserDetailsImpl userDetails = new UserDetailsImpl(usuario);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperaToken(HttpServletRequest request) {
        String cabecalhoAutorizacao = request.getHeader("Authorization");

        if (cabecalhoAutorizacao != null) return cabecalhoAutorizacao.replace("Bearer ", "");

        return null;
    }

    private boolean verificaSeEndpointEPublico(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        return Arrays.stream(SecurityConfiguration.ENDPOINTS_SEM_AUTENTICACAO)
                     .anyMatch(pattern -> requestURI.matches(pattern.replace("**", ".*")));
    }
}

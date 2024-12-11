package br.usp.projetoa.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.usp.projetoa.dto.JwtTokenDto;
import br.usp.projetoa.dto.LoginDto;
import br.usp.projetoa.security.JwtTokenService;
import br.usp.projetoa.security.UserDetailsImpl;


@Service
public class UsuarioAutenticavelService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public UsuarioAutenticavelService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public JwtTokenDto authenticateUser(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
            new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.senha());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new JwtTokenDto(jwtTokenService.geraToken(userDetails));
    }
}

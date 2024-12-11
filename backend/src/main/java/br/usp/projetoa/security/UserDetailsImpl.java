package br.usp.projetoa.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.usp.projetoa.model.UsuarioAutenticavel;

public class UserDetailsImpl implements UserDetails {
    private UsuarioAutenticavel usuario;

    public UserDetailsImpl(UsuarioAutenticavel usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getRole().toString()))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    public UsuarioAutenticavel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioAutenticavel usuario) {
        this.usuario = usuario;
    }
}

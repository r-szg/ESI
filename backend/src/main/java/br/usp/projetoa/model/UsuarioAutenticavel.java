package br.usp.projetoa.model;

import java.util.Set;

public interface UsuarioAutenticavel {
    String getUsername();
    String getPassword();
    Set<Role> getRoles();
}

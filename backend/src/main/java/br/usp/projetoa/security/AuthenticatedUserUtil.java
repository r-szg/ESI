package br.usp.projetoa.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUserUtil {
    public static Long getNumeroUspLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return Long.parseLong(userDetails.getUsername());
        }

        throw new IllegalStateException("Usuário não autenticado");
    }

    public static Set<String> getRolesLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities() != null) {
            return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        }

        throw new IllegalStateException("Usuário não autenticado");
    }

    public static boolean temPermissaoParaAcessarNumeroUsp(Long numeroUsp) {
        Long numeroUspLogado = getNumeroUspLogado();

        return numeroUsp.equals(numeroUspLogado);
    }
}

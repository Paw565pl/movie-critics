package dev.paw565pl.movie_critics.auth.util;

import dev.paw565pl.movie_critics.auth.role.Role;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public abstract class AuthUtil {

    public static boolean hasRole(Collection<? extends GrantedAuthority> authorities, Role role) {
        return authorities.stream().anyMatch(a -> a.getAuthority().equals(role.getPrefixedRole()));
    }
}

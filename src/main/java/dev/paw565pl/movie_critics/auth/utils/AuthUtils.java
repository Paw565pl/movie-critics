package dev.paw565pl.movie_critics.auth.utils;

import static dev.paw565pl.movie_critics.auth.Roles.ADMIN;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public abstract class AuthUtils {

    public static boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().anyMatch(a -> a.getAuthority().equals(ADMIN));
    }
}

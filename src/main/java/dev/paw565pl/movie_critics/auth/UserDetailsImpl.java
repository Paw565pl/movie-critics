package dev.paw565pl.movie_critics.auth;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

public class UserDetailsImpl implements UserDetails {

    @Getter private final UUID id;
    private final String username;
    @Getter private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    private UserDetailsImpl(
            UUID id, String username, String email, List<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
        this.email = email;
    }

    public static UserDetailsImpl fromJwt(Jwt jwt) {
        var id = UUID.fromString(jwt.getClaimAsString(JwtClaimNames.SUB));
        var username = jwt.getClaimAsString("preferred_username");
        var email = jwt.getClaimAsString("email");

        var realmAccess = jwt.getClaimAsMap("realm_access");
        var roles = (List<String>) realmAccess.get("roles");
        var authorities =
                roles.stream().map((r) -> new SimpleGrantedAuthority(r.toUpperCase())).toList();

        return new UserDetailsImpl(id, username, email, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }
}

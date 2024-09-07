package dev.paw565pl.movie_critics.auth.role;

public enum Role {
    ADMIN;

    /** Returns the role name with the 'ROLE_' prefix. */
    public String getPrefixedRole() {
        return "ROLE_" + name();
    }

    @Override
    public String toString() {
        return name();
    }
}

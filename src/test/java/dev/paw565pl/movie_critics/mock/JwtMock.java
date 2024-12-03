package dev.paw565pl.movie_critics.mock;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dev.paw565pl.movie_critics.auth.role.Role;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.oauth2.jwt.Jwt;

public abstract class JwtMock {

    public static String getTokenString(List<Role> roles) throws Exception {
        var claimsSet = new JWTClaimsSet.Builder()
                .subject("test")
                .issuer("self")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600000))
                .claim("sub", UUID.randomUUID())
                .claim("preferred_username", "test")
                .claim("email", "test@test.com")
                .claim("realm_access", Map.of("roles", roles))
                .build();
        var signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(
                new MACSigner(
                        "74ec9c9cbf5dbdb1974168aa270bdf9bf113db080e033a2e3f974190fe6244619e651fc3f53613776fb754995edb10ed7f5ac7fdac0c46368b3fa7e7ef9c763587c0170aa95fc4610c0533dda55f781890be9dafa3e63293826d2807a70e31384e60f319abd062fd59e8dccda5a76e440c5e14b1b6f5aca303639df77aea98cb8d78c67b12ebe327e761064c04d9059ff21215cebaa3f6ddb3c00dc4d01e839a5ba261fd06280f1f40df9c1131232b2cc16a53ce1e1847501dacd1aeab1936f24295f800faa4ec0f8ae0d816f36c3b760d99103c3ff058cc9ecdd0b0790741f7067e10558a7042bfa55d98522de69a1fc7de219f2849677711248f90345df5a2"));
        return signedJWT.serialize();
    }

    public static Jwt getToken(List<Role> roles) {
        var now = Instant.now();
        return Jwt.withTokenValue("mock-token")
                .header("alg", "HS256")
                .issuer("self")
                .header("typ", "JWT")
                .claim("sub", UUID.randomUUID())
                .claim("preferred_username", "test")
                .claim("email", "test@test.com")
                .claim("realm_access", Map.of("roles", roles))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .build();
    }
}

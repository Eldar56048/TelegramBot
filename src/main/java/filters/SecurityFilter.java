package filters;


import domain.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import services.AuthorizationService;
import services.interfaces.IAuthorizationService;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;


@Provider
@Priority(Priorities.AUTHORIZATION)
public class SecurityFilter implements ContainerRequestFilter {
    private final IAuthorizationService authService = new AuthorizationService();

    private static final String AUTHENTICATION_SCHEME = "Bearer ";

    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the Authorization header from the request

        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader
                .substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            // Validate the token
            final User user = validateToken(token);

            requestContext.setSecurityContext(new SecurityContext() {


                @Override
                public Principal getUserPrincipal() {
                    return (UserPrincipal) () -> user.getUsername();
                }

                @Override
                public boolean isUserInRole(String s) {
                    return (user.getRole() != null && user.getRole().equals(s));
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return null;
                }
            });

        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }



    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase());
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .build());
    }

    private User validateToken(String token) throws Exception {
        String secretWord = "TheStrongestSecretKeyICanThinkOf";
        Jws<Claims> result = Jwts.parserBuilder() // You can do with this result whatever you want
                .setSigningKey(Keys.hmacShaKeyFor(secretWord.getBytes()))
                .build().parseClaimsJws(token);

        User user = authService.getUserByUsername(result.getBody().getIssuer());
        return user;
    }


}

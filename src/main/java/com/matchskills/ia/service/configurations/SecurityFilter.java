package com.matchskills.ia.service.configurations;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.matchskills.ia.service.exceptions.customs.token.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final InternalTokenValidator validator;

    public SecurityFilter(InternalTokenValidator validator) {
        this.validator = validator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("X-Internal-Token");

        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            DecodedJWT decoded = validator.validate(header);
            String serviceName = decoded.getSubject();
            String role = decoded.getClaim("role").asString();

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            var auth = new UsernamePasswordAuthenticationToken(serviceName, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            log.debug("Chamada interna autenticada: subject={}, role={}", serviceName, role);
            filterChain.doFilter(request, response);

        } catch (TokenExpiredException e) {
            log.warn("Token de sistema expirado: {}", e.getMessage());
            reject(response, "Token expired");

        } catch (SignatureVerificationException e) {
            log.warn("Assinatura de token de sistema inválida — possível token forjado: {}", e.getMessage());
            reject(response, "invalid token");

        } catch (JWTVerificationException e) {
            log.warn("Token de sistema inválido: {}", e.getMessage());
            reject(response, "invalid token");
        }
    }

    private void reject(HttpServletResponse response, String message) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"" + message + ","+"\"}");
    }
}
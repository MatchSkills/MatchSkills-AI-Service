package com.matchskills.ia.service.configurations;

import com.matchskills.ia.service.enums.RoleType;
import com.matchskills.ia.service.exceptions.customs.token.TokenExpiredException;
import com.matchskills.ia.service.exceptions.customs.token.TokenInBlackListException;
import com.matchskills.ia.service.exceptions.customs.token.TokenInvalidTypeException;
import com.matchskills.ia.service.jwt.JwtService;
import com.matchskills.ia.service.services.RedisBlackListService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisBlackListService redisBlackListService;
    public SecurityFilter(JwtService jwtService,
                          RedisBlackListService redisBlackListService) {
        this.jwtService = jwtService;
        this.redisBlackListService = redisBlackListService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            var token = request.getHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {

                token = token.replace("Bearer ", "");

                redisBlackListService.verifyIfBlacklisted(jwtService.getTokenId(token));

                var decodedToken = jwtService.decodeToken(token);

                if (decodedToken.getRole().equals(RoleType.Candidate.name())){

                    var user = new UsernamePasswordAuthenticationToken(decodedToken.getUserId(), null, List.of(new SimpleGrantedAuthority("ROLE_" + RoleType.Candidate.name())));

                    SecurityContextHolder.getContext().setAuthentication(user);

                }

                if (decodedToken.getRole().equals(RoleType.Company.name())){

                    var user = new UsernamePasswordAuthenticationToken(decodedToken.getUserId(), null, List.of(new SimpleGrantedAuthority("ROLE_" + RoleType.Company.name())));

                    SecurityContextHolder.getContext().setAuthentication(user);

                }

                if (decodedToken.getRole().equals(RoleType.System.name())){

                    var user = new UsernamePasswordAuthenticationToken(decodedToken.getUserId(), null, List.of(new SimpleGrantedAuthority("ROLE_" + RoleType.System.name())));

                    SecurityContextHolder.getContext().setAuthentication(user);

                }

            }

            filterChain.doFilter(request, response);

        } catch (TokenExpiredException e){

            SecurityContextHolder.clearContext();

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("""
                    {\s
                        "status": 401,
                        "message": "Token is expired"
                     }\s
                   \s""");

        } catch (TokenInBlackListException e){

            SecurityContextHolder.clearContext();

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("""
                    {\s
                        "status": 401,
                        "message": "Token is blacklisted"
                     }\s
                   \s""");

        } catch (TokenInvalidTypeException e){

            SecurityContextHolder.clearContext();

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("""
                    {\s
                        "status": 401,
                        "message": "The type of this token is invalid"
                     }\s
                   \s""");
        }

    }
}

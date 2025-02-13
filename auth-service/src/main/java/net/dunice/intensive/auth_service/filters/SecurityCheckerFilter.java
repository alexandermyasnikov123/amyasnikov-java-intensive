package net.dunice.intensive.auth_service.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dunice.intensive.auth_service.services.JwtService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.Optional;

@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class SecurityCheckerFilter extends OncePerRequestFilter {
    private static final String NO_RESPONSE = "";

    private final JwtService jwtService;

    @Override
    @SneakyThrows
    public void doFilterInternal(
            @NotNull HttpServletRequest servletRequest,
            @NotNull HttpServletResponse servletResponse,
            @NotNull FilterChain chain
    ) {
        final var authHeaderValue = Optional.ofNullable(servletRequest.getHeader(HttpHeaders.AUTHORIZATION));
        final var hasAccess = authHeaderValue
                .filter(value -> value.startsWith(JwtService.BEARER_PREFIX))
                .map(value -> value.substring(JwtService.BEARER_PREFIX.length()))
                .map(jwtService::isValid)
                .orElse(true);

        if (hasAccess) {
            chain.doFilter(servletRequest, servletResponse);
        } else {
            servletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            servletResponse.getWriter().write(NO_RESPONSE);
        }
    }
}

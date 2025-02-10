package net.dunice.intensive.auth_service.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dunice.intensive.auth_service.services.JwtService;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class SecurityCheckerFilter implements Filter {
    private final JwtService jwtService;

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        if (request instanceof HttpServletRequest servletRequest) {
            final var authHeaderValue = Optional.ofNullable(servletRequest.getHeader(HttpHeaders.AUTHORIZATION));
            final var jwtToken = authHeaderValue
                    .filter(value -> value.startsWith(JwtService.BEARER_PREFIX))
                    .map(value -> value.substring(JwtService.BEARER_PREFIX.length()))
                    .orElse(null);

            if (jwtToken == null || jwtService.isValid(jwtToken)) {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}

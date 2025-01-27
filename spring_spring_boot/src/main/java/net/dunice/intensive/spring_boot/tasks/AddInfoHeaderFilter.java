package net.dunice.intensive.spring_boot.tasks;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Date;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class AddInfoHeaderFilter implements Filter {
    public static final String REQUEST_INFO_HEADER = "Date-Info-Header";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        final var requestWrapper = new MutableHttpServletRequestWrapper((HttpServletRequest) request);
        requestWrapper.putHeader(REQUEST_INFO_HEADER, new Date().toString());

        chain.doFilter(requestWrapper, response);
    }
}

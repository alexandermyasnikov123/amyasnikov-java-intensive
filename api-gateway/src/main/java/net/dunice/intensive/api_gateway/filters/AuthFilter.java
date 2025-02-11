package net.dunice.intensive.api_gateway.filters;

import lombok.RequiredArgsConstructor;
import net.dunice.intensive.api_gateway.clients.AuthServiceClient;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthFilter extends AbstractGatewayFilterFactory<Object> {
    private static final String NO_RESPONSE = "";

    @Lazy
    private final AuthServiceClient authServiceClient;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            final var serverRequest = exchange.getRequest();
            final var requestHeaders = serverRequest.getHeaders();

            return authServiceClient.checkAuth(requestHeaders).flatMap(responseEntity -> {
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    return chain.filter(exchange);
                } else {
                    final var responseWrapper = new ServerHttpResponseDecorator(exchange.getResponse()) {

                        @Override
                        public @NotNull Mono<Void> writeWith(@NotNull Publisher<? extends DataBuffer> body) {
                            final var response = exchange.getResponse();
                            final var newDataBuffer = response.bufferFactory().wrap(NO_RESPONSE.getBytes());
                            return getDelegate().writeWith(Mono.just(newDataBuffer));
                        }
                    };

                    return chain.filter(exchange.mutate().response(responseWrapper).build());
                }
            });
        };
    }
}

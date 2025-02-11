package net.dunice.intensive.api_gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

@FeignClient(value = "auth", url = "http://localhost:8082/api/v1/auth")
public interface AuthServiceClient {

    @GetMapping
    Mono<ResponseEntity<Object>> checkAuth(@RequestHeader HttpHeaders headers);
}

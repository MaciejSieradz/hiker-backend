package com.example.hiker.interceptor;

import com.example.hiker.model.User;
import com.example.hiker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserCheckInterceptor implements WebFilter {

    private final UserRepository userRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    var jwt = (Jwt) securityContext.getAuthentication().getPrincipal();
                    return userRepository.findByEmail(retrieveEmailFromToken(jwt))
                            .switchIfEmpty(createUser(jwt)).then();
                }).then(chain.filter(exchange));
    }

    private Mono<User> createUser(Jwt jwt) {
        var newUser = new User();
        newUser.setEmail(jwt.getClaimAsString("email"));
        newUser.setUsername(jwt.getClaimAsString("name"));
        newUser.setAvatarUrl(jwt.getClaimAsString("picture"));

        return userRepository.save(newUser);
    }

    private String retrieveEmailFromToken(Jwt jwt) {
        return jwt.getClaimAsString("email");
    }
}

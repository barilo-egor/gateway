package tgb.cryptoexchange.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationManager jwtAuthManager;
    private final JwtSecurityContextRepository contextRepository;

    public SecurityConfig(JwtAuthenticationManager jwtAuthManager,
                                 JwtSecurityContextRepository contextRepository) {
        this.jwtAuthManager = jwtAuthManager;
        this.contextRepository = contextRepository;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/login").permitAll()
                        .anyExchange().authenticated()
                )
                .authenticationManager(jwtAuthManager)
                .securityContextRepository(contextRepository)
                .build();
    }
}

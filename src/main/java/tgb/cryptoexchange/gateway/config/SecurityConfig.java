package tgb.cryptoexchange.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationManager jwtAuthManager;

    private final JwtSecurityContextRepository contextRepository;

    private final AppSecurityProperties appSecurityProperties;

    public SecurityConfig(JwtAuthenticationManager jwtAuthManager,
                          JwtSecurityContextRepository contextRepository, AppSecurityProperties appSecurityProperties) {
        this.jwtAuthManager = jwtAuthManager;
        this.contextRepository = contextRepository;
        this.appSecurityProperties = appSecurityProperties;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> {
                    ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec = exchange
                            .pathMatchers("/auth/login").permitAll();
                    if (!appSecurityProperties.getIgnoreUrls().isEmpty()) {
                        authorizeExchangeSpec
                                .pathMatchers(appSecurityProperties.getIgnoreUrls().toArray(String[]::new)).permitAll();
                    }
                    authorizeExchangeSpec.anyExchange().authenticated();
                })
                .authenticationManager(jwtAuthManager)
                .securityContextRepository(contextRepository)
                .build();
    }
}

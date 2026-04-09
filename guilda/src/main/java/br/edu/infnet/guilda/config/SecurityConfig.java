package br.edu.infnet.guilda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.security.admin.user}")
    private String adminUser;

    @Value("${app.security.admin.pass}")
    private String adminPass;

    @Value("${app.security.user.user}")
    private String defaultUser;

    @Value("${app.security.user.pass}")
    private String defaultPass;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/actuator/info").permitAll()
                        .requestMatchers("/aventureiros/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username(adminUser)
                .password("{noop}" + adminPass) // {noop} is used to store plain text password for this demo
                .roles("ADMIN")
                .build();
        
        UserDetails user = User.builder()
                .username(defaultUser)
                .password("{noop}" + defaultPass)
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}

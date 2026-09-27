package ru.netology.jdbc_api_jdbctemplate_hw;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/products/fetch-product").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails readUser = User.builder()
                .username("read_user")
                .password("password")
                .roles("READ")
                .build();
        UserDetails writeUser = User.builder()
                .username("write_user")
                .password("password")
                .roles("WRITE")
                .build();
        UserDetails deleteUser = User.builder()
                .username("delete_user")
                .password("password")
                .roles("DELETE")
                .build();
        UserDetails multiUser = User.builder()
                .username("multi_user")
                .password("password")
                .roles("WRITE", "DELETE")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("password")
                .roles("READ", "WRITE", "DELETE")
                .build();

        return new InMemoryUserDetailsManager(readUser, writeUser, deleteUser, multiUser, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
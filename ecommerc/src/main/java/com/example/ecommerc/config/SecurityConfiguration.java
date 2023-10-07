package com.example.ecommerc.config;

import com.example.ecommerc.repository.RoleRepository;
import com.example.ecommerc.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf((csrf)->csrf.disable())
//                .cors(cors -> cors.disable())
                .authorizeHttpRequests((authorize)->{
                    authorize.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll();
                    authorize.requestMatchers("/home/**","/authenticate","/register","/search","/product-list/**","/product/**","/upload/**").permitAll();
                    authorize.requestMatchers("/admin/**").hasRole("ADMIN");
                    //authorize.requestMatchers("/change-password").hasAnyRole("ADMIN","USER");
                    authorize.anyRequest().authenticated();
                }).sessionManagement((sess)->{
                    sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

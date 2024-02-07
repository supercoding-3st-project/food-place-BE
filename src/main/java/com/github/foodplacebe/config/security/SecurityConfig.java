package com.github.foodplacebe.config.security;

import com.github.foodplacebe.config.security.exception.CustomAccessDeniedHandler;
import com.github.foodplacebe.config.security.exception.CustomAuthenticationEntryPoint;
import com.github.foodplacebe.web.filters.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenConfig jwtTokenConfig;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(h->h.frameOptions(f->f.sameOrigin()))
                .csrf((c)->c.disable())
                .httpBasic((h)->h.disable())
                .formLogin(f->f.disable())

                .rememberMe(r->r.disable())
                .cors(c->{
                    c.configurationSource(corsConfigurationSource());
                })
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .exceptionHandling(e->{
                    e.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                    e.accessDeniedHandler(new CustomAccessDeniedHandler());
                })
                .authorizeRequests(a ->
                            a
                                    .requestMatchers("/auth/test9").hasRole("ADMIN")
                                    .requestMatchers("/auth/logout","/v1/api/account/my-page","/v1/api/mypost","/account/*",
                                            "/v1/api/reg-post","/v1/api/modify-post/*","/v1/api/post-like-heart/*",
                                            "/v1/api/post-heart-status/*","/v1/api/delete-food/*","/v1/api/comment/add","/v1/api/comment/mod",
                                            "/v1/api/comment/del/*","/v1/api/comment/like/*")
                                        .hasAnyRole("USER","ADMIN")
                                    .requestMatchers("/resources/static/**","/v1/api/test", "/auth/**","/").permitAll()


                )
                .addFilterBefore(new JwtFilter(jwtTokenConfig), UsernamePasswordAuthenticationFilter.class);
    return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
//        corsConfiguration.setAllowCredentials(true); 쿠키세션방식
        corsConfiguration.addExposedHeader("Token");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","PUT","POST","PATCH","DELETE","OPTIONS"));
        corsConfiguration.setMaxAge(1000L*60*60);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

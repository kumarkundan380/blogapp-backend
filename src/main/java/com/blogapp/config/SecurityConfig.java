package com.blogapp.config;

import com.blogapp.enums.UserRole;
import com.blogapp.security.JwtAuthEntryPoint;
import com.blogapp.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.blogapp.constant.BlogAppConstant.PUBLIC_URLS;
import static com.blogapp.constant.BlogAppConstant.BASE_PATH_ROLE;
import static com.blogapp.constant.BlogAppConstant.BASE_PATH_USER;

@Configuration
@EnableWebMvc
public class SecurityConfig {
	private final AuthenticationProvider authenticationProvider;
	@Autowired
	public SecurityConfig(AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
    		JwtAuthEntryPoint jwtAuthEntryPoint,
    		JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests( request ->
					request.requestMatchers(PUBLIC_URLS).permitAll()
						.requestMatchers(HttpMethod.POST, BASE_PATH_USER).permitAll()
						.requestMatchers(BASE_PATH_ROLE).hasAuthority(UserRole.ADMIN.getValue())
						.anyRequest()
						.authenticated())
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(config -> config.authenticationEntryPoint(jwtAuthEntryPoint))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.blogapp.constant.BlogAppConstant.ADMIN_URLS;
import static com.blogapp.constant.BlogAppConstant.APPROVED_POST;
import static com.blogapp.constant.BlogAppConstant.BASE_PATH_ADMIN;
import static com.blogapp.constant.BlogAppConstant.BASE_PATH_CATEGORY;
import static com.blogapp.constant.BlogAppConstant.BASE_PATH_POST;
import static com.blogapp.constant.BlogAppConstant.BASE_PATH_USER;
import static com.blogapp.constant.BlogAppConstant.DELETED_POST;
import static com.blogapp.constant.BlogAppConstant.PENDING_POST;
import static com.blogapp.constant.BlogAppConstant.PUBLIC_URLS;
import static com.blogapp.constant.BlogAppConstant.ROLES_PATH;

@Configuration
@EnableWebSecurity
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
							.requestMatchers(HttpMethod.GET, BASE_PATH_CATEGORY).permitAll()
							.requestMatchers(HttpMethod.GET, BASE_PATH_POST + APPROVED_POST).permitAll()
							.requestMatchers(HttpMethod.GET, BASE_PATH_POST + "/**").permitAll()
							.requestMatchers(HttpMethod.DELETE,BASE_PATH_USER + "/**").hasAuthority(UserRole.ADMIN.getValue())
							.requestMatchers(HttpMethod.POST,BASE_PATH_CATEGORY + "/**").hasAuthority(UserRole.ADMIN.getValue())
							.requestMatchers(HttpMethod.PUT,BASE_PATH_CATEGORY + "/**").hasAuthority(UserRole.ADMIN.getValue())
							.requestMatchers(HttpMethod.DELETE,BASE_PATH_CATEGORY + "/**").hasAuthority(UserRole.ADMIN.getValue())
							.requestMatchers(HttpMethod.GET, BASE_PATH_POST + PENDING_POST).hasAuthority(UserRole.ADMIN.getValue())
							.requestMatchers(HttpMethod.GET, BASE_PATH_POST + DELETED_POST).hasAuthority(UserRole.ADMIN.getValue())
							.requestMatchers(HttpMethod.GET,BASE_PATH_ADMIN).hasAuthority(UserRole.ADMIN.getValue())
							.requestMatchers(HttpMethod.DELETE,ADMIN_URLS).hasAuthority(UserRole.ADMIN.getValue())
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

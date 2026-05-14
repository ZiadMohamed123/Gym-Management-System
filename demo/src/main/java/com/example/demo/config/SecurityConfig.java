package com.example.demo.config;

import java.util.Collection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/",
					"/login",
					"/signup",
					"/landing",
					"/css/**",
					"/js/**",
					"/images/**",
					"/h2-console/**"
				).permitAll()
				.requestMatchers("/dashboard").hasRole("ADMIN")
				.requestMatchers("/trainer/home").hasRole("TRAINER")
				.requestMatchers("/member/home").hasRole("MEMBER")
				.requestMatchers("/members/register/**", "/members/update/**", "/members/search").hasRole("ADMIN")
				.requestMatchers("/members/status").hasAnyRole("ADMIN", "TRAINER")
				.requestMatchers("/members/profile/**").hasAnyRole("ADMIN", "MEMBER")
				.requestMatchers("/trainers/**").hasRole("ADMIN")
				.requestMatchers("/memberships/**", "/subscriptions/**", "/attendance/**").hasRole("ADMIN")
				.requestMatchers("/classes/create", "/classes/update/**", "/classes/cancel/**")
					.hasAnyRole("ADMIN", "TRAINER")
				.requestMatchers("/classes/participants/**").hasAnyRole("ADMIN", "TRAINER")
				.requestMatchers("/classes/**").hasAnyRole("ADMIN", "TRAINER", "MEMBER")
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.successHandler(authenticationSuccessHandler())
				.permitAll()
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.permitAll()
			)
			.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
			.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return (request, response, authentication) -> {
			String target = resolveTarget(authentication.getAuthorities());
			response.sendRedirect(request.getContextPath() + target);
		};
	}

	private String resolveTarget(Collection<? extends GrantedAuthority> authorities) {
		if (hasRole(authorities, "ROLE_ADMIN")) {
			return "/dashboard";
		}
		if (hasRole(authorities, "ROLE_TRAINER")) {
			return "/trainer/home";
		}
		if (hasRole(authorities, "ROLE_MEMBER")) {
			return "/member/home";
		}
		return "/";
	}

	private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
		return authorities.stream().anyMatch(auth -> auth.getAuthority().equals(role));
	}
}

package com.epp.interino.interns;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		     http
		             .authorizeHttpRequests(request -> request
		                     .requestMatchers("/interns/**")
		                     .hasRole("BOSS"))
		             .httpBasic(Customizer.withDefaults())
		             .csrf(csrf -> csrf.disable());
		     return http.build();
		}

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
    	User.UserBuilder users = User.builder();
    	UserDetails javier = users
    		.username("Javier")
    		.password(passwordEncoder.encode("j"))
    		.roles("BOSS")
    		.build();
    	UserDetails hankOwnsNoCards = users
    		.username("hank-owns-no-cards")
    		.password(passwordEncoder.encode("qrs456"))
    		.roles("NON-BOSS")
    		.build();
    	UserDetails rosa = users
    		.username("Rosa")
    		.password(passwordEncoder.encode("r"))
    		.roles("BOSS")
    		.build();
    	return new InMemoryUserDetailsManager(javier, hankOwnsNoCards, rosa);
    }
    
}

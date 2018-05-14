package com.feedit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userService;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    		.csrf()
    			.disable()
    		.authorizeRequests()
			.antMatchers(
					"/registration", 
					"/js/**", 
					"/css/**", 
					"/img/**", 
					"/webjars/**").permitAll()
			.anyRequest().authenticated()
    			.antMatchers("/", "/home", "/about", "/h2/**").permitAll()
    		.antMatchers("/admin/**").hasRole("ADMIN")
    		.antMatchers("/user/**").hasRole("USER")
    		.anyRequest().authenticated()
    		.and()
    			.formLogin()
    				.loginPage("/")
    						.permitAll()
    		.and()
    			.logout()
    				.permitAll()
    		.and()
    			.headers()
    				.frameOptions()
    					.disable();
    }

    @Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
}
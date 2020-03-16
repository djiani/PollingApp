package com.revature.PollingApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.revature.PollingApp.security.CustomUserDetailsService;
import com.revature.PollingApp.security.JwtAuthenticationEntryPoint;
import com.revature.PollingApp.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity  //primary spring security annotation used to enable web security in project
@EnableGlobalMethodSecurity( //enable method level security based on annotation
		securedEnabled = true,  //it enables @secured annotation ex: @Secured("ROLE_ADMIN") Public Users getAllUsers()
		jsr250Enabled = true,  //it enables @RolesAllowed annotation ex: @RolesAllowed("ROLE_ADMIN") Public Poll createPoll()
		prePostEnabled = true) //it enables more complex expression based access control syntax with @PreAuthorize and @PostAuthorize

public class SecurityConfig  extends WebSecurityConfigurerAdapter{ 
	
	//load userDetails
    @Autowired
    CustomUserDetailsService customUserDetailsService; 

    //return 401 unauthorized error
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler; 

    //filter and manager jwt token
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() { 
        return new JwtAuthenticationFilter();
    }

    //main security interface for authenticating user. we provide a customUserDetails and passwordEncorder 
    //to build the authenticationManager
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    
    //expose authenticationManager as a bean
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    //configure security functionalities and add rules to protect resources based on varions conditions
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .and()
                .csrf()
                    .disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                        .permitAll()
                    .antMatchers("/api/auth/**")
                        .permitAll()
                    .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability")
                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**")
                        .permitAll()
                    .anyRequest()
                        .authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

}

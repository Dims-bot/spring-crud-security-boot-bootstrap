package com.javamentor.springcrudsecuritybootfrom1.configuration;

import com.javamentor.springcrudsecuritybootfrom1.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


    private UserDetailsServiceImpl userDetailsService;
    //private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    public WebSecurityConfiguration(UserDetailsServiceImpl userDetailsService,
            LoginSuccessHandler loginSuccessHandler) {
        this.userDetailsService = userDetailsService;
       // this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Override
    protected void configure
            (AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
                authorizeRequests()
                .antMatchers("/login").anonymous()
                .antMatchers("/users/admin/**").hasRole("ADMIN")
                .antMatchers("/users/user").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and().

                formLogin()

                .loginPage("/login")
                .failureUrl("/login-error")
                .successHandler(new LoginSuccessHandler())
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()

                .logout()

                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .and().csrf().disable();


    }
}

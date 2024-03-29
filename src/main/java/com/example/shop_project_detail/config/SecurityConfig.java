package com.example.shop_project_detail.config;

import com.example.shop_project_detail.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;


@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
// if using annotation method for endpoint protection, uncomment @PreAuthorize in ContentController and @EnableGlobalMethodSecurity here
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtFilter jwtFilter;

    @Autowired
    public void setJwtFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/user/*").hasAuthority("user")
                .antMatchers("/admin/*").hasAuthority("admin")
                .anyRequest().authenticated();
    }



}

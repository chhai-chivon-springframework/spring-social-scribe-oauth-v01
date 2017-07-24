package com.phengtola.ui.configurations.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Created by tolapheng on 7/17/17.
 */

@Configuration
public class ArticleWebSecurity extends WebSecurityConfigurerAdapter{


    /***
     * - "/","/home","/about","/article" : Allow all requests
     * - Role admin allow to access "/admin/**"
     * - Role user allow to access "/article/**"
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/facebook/**").permitAll()
                .antMatchers("/profile").authenticated()
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();

    }




    /**
     * Create in memory users:
     * + User 1:
     * 	- Username: user
     * 	- Password: 123
     * 	- Roles: USER
     * + User 2:
     * 	- Username: admin
     * 	- Password: 123
     *  - Roles: USER and ADMIN
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("swagger").password("123456").roles("API_DEV");
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
        web.ignoring().antMatchers("/static/**");
    }


}
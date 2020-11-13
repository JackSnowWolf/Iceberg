//package com.iceberg.security.config;
//
//import com.iceberg.security.oauth.CustomOAuth2UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class SecruityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private CustomOAuth2UserService oAuth2UserService;
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.authorizeRequests()
//                .antMatchers("/oauth/**","/login**","/","static").permitAll()
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                    .loginPage("/login")
//                    .usernameParameter("email")
//                    .permitAll()
//                    .defaultSuccessUrl("/pages/index")
//                .and()
//                .oauth2Login()
//                    .loginPage("/login")
//                    .userInfoEndpoint().userService(oAuth2UserService)
//                    .and()
//                .and()
//                .logout().permitAll();
//    }
//}

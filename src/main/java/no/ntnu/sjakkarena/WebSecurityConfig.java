package no.ntnu.sjakkarena;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        JWSFilter jwsFilter = new JWSFilter();
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/new-tournament").permitAll()
                .antMatchers("/new-user").permitAll()
                .antMatchers("/tournament/**").hasAuthority("TOURNAMENT")
                .antMatchers("/user/**").hasAuthority("USER")
                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(jwsFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

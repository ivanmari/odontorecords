package odontograme;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * Created by immari on 11/17/2016.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter{

    /**
     * This section defines the user accounts which can be used for authentication as well as the roles each user has.
     *
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication().//
                withUser("greg").password("turnquist").roles("USER").and().//
                withUser("ollie").password("gierke").roles("USER", "ADMIN");
    }

    /**
     * This section defines the security policy for the app.
     *
     * BASIC authentication is supported
     * @param http
     * @throws Exception
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().and().authorizeRequests().//
                antMatchers(HttpMethod.GET, "/patients").hasRole("USER").//
                antMatchers(HttpMethod.POST, "/patients").hasRole("ADMIN").//
                antMatchers(HttpMethod.PUT, "/patients/**").hasRole("ADMIN").//
                antMatchers(HttpMethod.PATCH, "/patients/**").hasRole("ADMIN").and().//
                csrf().disable();
    }
}

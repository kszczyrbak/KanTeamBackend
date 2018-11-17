package szczkrzy.kanteam.config;

import ch.qos.logback.core.net.server.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import szczkrzy.kanteam.security.ClientTokenInterceptor;
import szczkrzy.kanteam.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationEntryPoint unathorizedHandler;

    private final JwtAuthenticationFilter authenticationTokenFilter;

    private final UserDetailsService userDetailsService;

    private final ClientTokenInterceptor clientTokenInterceptor;


    @Autowired
    public WebSecurityConfig(PasswordEncoder passwordEncoder,
                             AuthenticationEntryPoint unathorizedHandler,
                             JwtAuthenticationFilter authenticationTokenFilter,
                             UserDetailsService userDetailsService,
                             ClientTokenInterceptor clientTokenInterceptor) {
        this.passwordEncoder = passwordEncoder;
        this.unathorizedHandler = unathorizedHandler;
        this.authenticationTokenFilter = authenticationTokenFilter;
        this.userDetailsService = userDetailsService;
        this.clientTokenInterceptor = clientTokenInterceptor;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/api/*").authenticated()
                .antMatchers("/auth/*", "/**").permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(unathorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

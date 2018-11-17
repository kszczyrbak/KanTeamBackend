package szczkrzy.kanteam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import szczkrzy.kanteam.model.entities.KTUser;
import szczkrzy.kanteam.security.ClientTokenInterceptor;
import szczkrzy.kanteam.security.AuthenticationErrorHandler;
import szczkrzy.kanteam.security.JwtAuthenticationFilter;
import szczkrzy.kanteam.security.JwtTokenService;
import szczkrzy.kanteam.services.SecurityDetailsService;

@Configuration
public class BeanConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public JwtTokenService tokenService() {
        return new JwtTokenService();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new SecurityDetailsService();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationErrorHandler();
    }

    @Bean
    public ClientTokenInterceptor clientTokenInterceptor() {
        return new ClientTokenInterceptor(requestUser());
    }

    @Bean(name = "requestUser")
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public KTUser requestUser() {
        return new KTUser();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(clientTokenInterceptor());
    }
}

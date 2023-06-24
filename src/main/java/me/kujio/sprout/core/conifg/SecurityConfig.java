package me.kujio.sprout.core.conifg;

import lombok.RequiredArgsConstructor;
import me.kujio.sprout.core.annotation.Anonymous;
import me.kujio.sprout.core.filter.JwtFilter;
import me.kujio.sprout.core.resolver.ExceptionResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;
    private final ExceptionResolver exceptionResolver;
    private final ApplicationContext applicationContext;
    private final UserDetailsService userDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers(anonymousUrls()).permitAll();

        http.authorizeRequests().antMatchers("/upload/**","download/**","/resource/**").permitAll();

        http.authorizeRequests().anyRequest().authenticated();

        http.exceptionHandling().authenticationEntryPoint(exceptionResolver);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    private String[] anonymousUrls() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        Set<String> urls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            Anonymous method = AnnotationUtils.findAnnotation(entry.getValue().getMethod(), Anonymous.class);
            if (method != null && entry.getKey().getPathPatternsCondition() != null) {
                for (String url : entry.getKey().getPathPatternsCondition().getPatternValues()) {
                    urls.add(url.replaceAll("\\{(.*?)}", "*"));
                }
            }
            Anonymous controller = AnnotationUtils.findAnnotation(entry.getValue().getBeanType(), Anonymous.class);
            if (controller != null && entry.getKey().getPathPatternsCondition() != null) {
                for (String url : entry.getKey().getPathPatternsCondition().getPatternValues()) {
                    urls.add(url.replaceAll("\\{(.*?)}", "*"));
                }
            }
        }
        return urls.toArray(new String[0]);
    }
}

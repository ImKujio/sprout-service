package me.kujio.sprout.core.filter;

import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.service.SysLoginService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final SysLoginService sysLoginService;

    public JwtFilter(SysLoginService sysLoginService) {
        this.sysLoginService = sysLoginService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        AuthInfo authInfo = sysLoginService.parseToken(request,response);
        if (authInfo != null){
            authInfo.setParameterMap(request.getParameterMap());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authInfo,null,authInfo.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }
}

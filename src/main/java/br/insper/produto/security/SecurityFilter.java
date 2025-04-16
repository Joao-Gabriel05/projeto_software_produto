package br.insper.produto.security;

import br.insper.produto.login.LoginService;
import br.insper.produto.usuario.Usuario;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private LoginService loginService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if ( method.equals(HttpMethod.POST.name())) {
            String token = request.getHeader("Authorization");
            Usuario user = loginService.validateToken(token);
            if(user.getPapel().equals("ADMIN")){
            filterChain.doFilter(request, response);
            }
            else{
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Método não permitido para esse usuário");
            }
        } else {

            filterChain.doFilter(request, response);
        }
    }


}

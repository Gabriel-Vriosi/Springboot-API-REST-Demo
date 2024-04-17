package com.prueba.springbootapp.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.prueba.springbootapp.services.JwtService;
import com.prueba.springbootapp.services.UserDetailsServiceImp;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserDetailsServiceImp userDetailsServiceImp;

    @Override
    protected void doFilterInternal(
        @NotBlank HttpServletRequest request,
        @NotBlank HttpServletResponse response,
        @NotBlank FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        final String username = jwtService.extractUsername(token);

        if (
            username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsServiceImp
                .loadUserByUsername(username);

            if (jwtService.isValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

                authToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder
                .getContext()
                .setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

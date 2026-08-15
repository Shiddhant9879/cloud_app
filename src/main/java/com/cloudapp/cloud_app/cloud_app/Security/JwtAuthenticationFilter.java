package com.cloudapp.cloud_app.cloud_app.Security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.cloudapp.cloud_app.cloud_app.Service.CustomUserDetailService;
import com.cloudapp.cloud_app.cloud_app.Security.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailService customUserDetailService;

    public JwtAuthenticationFilter(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // check the authorization header for a JWT token
        String authorizationHeader = request.getHeader("Authorization");

        // if header missing or doesn't start with Bearer -> skip processing, let
        // request continue
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // extract the token
        String token = authorizationHeader.substring(7);

        // extract the username from the token
        String username = JwtUtil.extractUsername(token);

        // check if username exists and not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // load user details from the database
            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

            // validate the token
            boolean isValid = JwtUtil.validateToken(token);

            if (isValid) {

                // build the authentication object
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // set it in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // continue the filter chain
        filterChain.doFilter(request, response);
    }
}
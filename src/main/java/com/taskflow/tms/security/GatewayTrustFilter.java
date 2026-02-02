package com.taskflow.tms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

/**
 * Gateway Trust Filter for Task Service
 * Extracts user information from headers set by the API Gateway
 * and establishes security context for authorization
 */
@Component
public class GatewayTrustFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract user ID from header set by API Gateway
        String userIdHeader = request.getHeader("X-User-Id");
        String emailHeader = request.getHeader("X-User-Email");

        if (userIdHeader != null && !userIdHeader.isEmpty()) {
            try {
                // Parse user ID (could be UUID from local DB or Keycloak sub claim)
                String userId = userIdHeader;
                
                // Create authentication token with user info
                // The principal is the user ID, which can be used in controllers
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                        );

                // Store email in details for additional context
                authentication.setDetails(emailHeader);

                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            } catch (Exception e) {
                logger.error("Error parsing user ID from header: " + userIdHeader, e);
            }
        }

        filterChain.doFilter(request, response);
    }
}

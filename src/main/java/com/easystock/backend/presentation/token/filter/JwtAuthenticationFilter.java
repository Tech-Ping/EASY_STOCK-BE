package com.easystock.backend.presentation.token.filter;

import com.easystock.backend.aspect.exception.UnauthorizedTokenException;
import com.easystock.backend.presentation.token.jwt.JwtGenerator;
import com.easystock.backend.presentation.token.jwt.JwtProvider;
import com.easystock.backend.presentation.token.UserAuthentication;
import com.easystock.backend.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.easystock.backend.presentation.token.UserAuthentication.createUserAuthentication;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtGenerator jwtGenerator;
    private final JwtProvider jwtProvider;

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/auth", "/swagger-ui/", "/token", "/v3/api-docs"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException{
        String path = request.getRequestURI();
        if(isExcludedPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = getAccessToken(request);
        if(!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtGenerator.validateAccessToken(accessToken)) {
                Long memberId = jwtProvider.getSubject(accessToken);
                UserAuthentication authentication = UserAuthentication.createUserAuthentication(memberId);
                setAuthentication(request, authentication);
            }
        } catch (UnauthorizedTokenException ex) {
            String refreshToken = getRefreshToken(request);
            if (StringUtils.hasText(refreshToken) && jwtGenerator.validateRefreshToken(refreshToken)) {
                Long memberId = jwtProvider.getMemberIdFromRefreshToken(refreshToken);
                String newAccessToken = jwtGenerator.generateAccessToken(memberId);
                response.setHeader(Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX + newAccessToken);
                UserAuthentication authentication = UserAuthentication.createUserAuthentication(memberId);
                setAuthentication(request, authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid or missing tokens.\"}");
                return;            }
        }

        filterChain.doFilter(request, response);
    }


    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::contains);
    }

    private String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(Constants.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(accessToken) && accessToken.startsWith(Constants.BEARER_PREFIX)) {
            return accessToken.substring(Constants.BEARER_PREFIX.length());
        }
        return null;
    }

    private void setAuthentication(HttpServletRequest request, UserAuthentication authentication) {
        WebAuthenticationDetailsSource source = new WebAuthenticationDetailsSource();
        WebAuthenticationDetails details = source.buildDetails(request);
        authentication.setDetails(details);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getRefreshToken(HttpServletRequest request) {
        return request.getHeader(Constants.REFRESH_TOKEN_HEADER);
    }
}

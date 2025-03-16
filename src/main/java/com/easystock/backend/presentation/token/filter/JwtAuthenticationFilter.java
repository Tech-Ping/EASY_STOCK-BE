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

import static com.easystock.backend.presentation.token.UserAuthentication.createUserAuthentication;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtGenerator jwtGenerator;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException{
        String path = request.getRequestURI();

        if(path.contains("/api/auth") || path.contains("/swagger-ui/") || path.contains("/token") || path.contains("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String accessToken = getAccessToken(request);
        try {
            if (jwtGenerator.validateAccessToken(accessToken)) {
                Long memberId = jwtProvider.getSubject(accessToken);
                UserAuthentication authentication = createUserAuthentication(memberId);
                createAndSetWebAuthenticationDetails(request, authentication);
            }
        } catch (UnauthorizedTokenException ex) {
            String refreshToken = getRefreshToken(request);
            if (refreshToken != null && jwtGenerator.validateRefreshToken(refreshToken)) {
                Long memberId = jwtProvider.getMemberIdFromRefreshToken(refreshToken);
                String newAccessToken = jwtGenerator.generateAccessToken(memberId);
                response.setHeader(Constants.AUTHORIZATION_HEADER, Constants.BEARER_PREFIX + newAccessToken);
                UserAuthentication authentication = createUserAuthentication(memberId);
                createAndSetWebAuthenticationDetails(request, authentication);
            } else {
                handleUnAuthorizedResponse(response, "Invalid refresh token."); return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleUnAuthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    private String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(Constants.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(accessToken) && accessToken.startsWith(Constants.BEARER_PREFIX)) {
            return accessToken.substring(Constants.BEARER_PREFIX.length());
        }
        return null;
    }

    private void createAndSetWebAuthenticationDetails(HttpServletRequest request, UserAuthentication authentication) {
        WebAuthenticationDetailsSource webAuthenticationDetailsSource = new WebAuthenticationDetailsSource();
        WebAuthenticationDetails webAuthenticationDetails = webAuthenticationDetailsSource.buildDetails(request);
        authentication.setDetails(webAuthenticationDetails);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

    }

    private String getRefreshToken(HttpServletRequest request) {
        return request.getHeader(Constants.REFRESH_TOKEN_HEADER);
    }
}

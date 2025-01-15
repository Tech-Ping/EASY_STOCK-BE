package com.easystock.backend.presentation.token.filter;

import com.easystock.backend.aspect.exception.UnauthorizedTokenException;
import com.easystock.backend.presentation.token.jwt.JwtGenerator;
import com.easystock.backend.presentation.token.jwt.JwtProvider;
import com.easystock.backend.presentation.token.UserAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.easystock.backend.presentation.token.UserAuthentication.createUserAuthentication;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtGenerator jwtTokenProvider;
    private final JwtProvider jwtProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){
        final String accessToken = getAccessToken(request);
        jwtTokenProvider.validateAccessToken(accessToken);
        doAuthentication(request, jwtProvider.getSubject(accessToken));
    }

    private String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            return accessToken.substring(BEARER_PREFIX.length());
        }
        throw new UnauthorizedTokenException();
    }

    private void doAuthentication(HttpServletRequest request, Long memberId) {
        UserAuthentication authentication = createUserAuthentication(memberId);
        createAndSetWebAuthenticationDetails(request, authentication);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    private void createAndSetWebAuthenticationDetails(HttpServletRequest request, UserAuthentication authentication) {
        WebAuthenticationDetailsSource webAuthenticationDetailsSource = new WebAuthenticationDetailsSource();
        WebAuthenticationDetails webAuthenticationDetails = webAuthenticationDetailsSource.buildDetails(request);
        authentication.setDetails(webAuthenticationDetails);
    }
}

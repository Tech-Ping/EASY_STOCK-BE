package com.easystock.backend.presentation.token;

import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Configuration
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter){
        boolean hasMemberIdAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
        boolean isRightType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasMemberIdAnnotation && isRightType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication auth) {
            Object principal = auth.getPrincipal();
            return principal;
        }
        throw new IllegalArgumentException("Invalid principal type or authentication type");
    }
}

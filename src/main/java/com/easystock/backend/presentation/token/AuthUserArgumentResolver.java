package com.easystock.backend.presentation.token;

import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter){
        boolean hasMemberIdAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
        boolean isLongType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasMemberIdAnnotation && isLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory)
    {
        UserAuthentication authentication =
                (UserAuthentication)  SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof Long) {
            return authentication.getPrincipal();
        }
        throw new AuthException(ErrorStatus.AUTH_UNKNOWN_MEMBER_ID);
    }
}

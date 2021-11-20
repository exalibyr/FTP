package com.excalibur.ftp.interceptor;

import com.excalibur.ftp.configuration.AuthConfiguration;
import com.excalibur.ftp.exception.Exceptions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Component
public class AuthWebInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthConfiguration authConfiguration;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!authConfiguration.isActive() || authConfiguration.getValue().equals(request.getHeader(authConfiguration.getName()))) {
            return true;
        } else {
            throw Exceptions.unauthorized("Illegal auth data!");
        }
    }
}

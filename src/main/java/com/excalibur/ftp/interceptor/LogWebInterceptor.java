package com.excalibur.ftp.interceptor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Component
public class LogWebInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("==================================== REQUEST BEGIN ====================================");
        log.info("Inbound {} request to {} ", request.getMethod(), request.getRequestURI());
        log.info("Session ID: {}", request.getSession().getId());
        log.info("Request params: {}", request.getParameterMap());
        log.info("==================================== REQUEST END ====================================");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("==================================== RESPONSE BEGIN ====================================");
        log.info("Outbound response to {} ", request.getRemoteAddr());
        log.info("Session ID: {}", request.getSession().getId());
        log.info("Status code: {} ", response.getStatus());
        log.info("==================================== RESPONSE END ====================================");
    }
}

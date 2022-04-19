package com.easonhotel.admin.security;

import com.easonhotel.common.ResData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter printWriter = response.getWriter();
        ResData resData;
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            resData = ResData.create(-1, "账号或密码错误");
        } else if (exception instanceof DisabledException) {
            resData = ResData.create(-1, "账户被锁");
        } else {
            resData = ResData.create(-1, "系统异常,登录失败");
        }
        printWriter.write(objectMapper.writeValueAsString(resData));
        printWriter.flush();
        printWriter.close();
    }
}

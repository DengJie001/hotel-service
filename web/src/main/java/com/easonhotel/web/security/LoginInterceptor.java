package com.easonhotel.web.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.UserInfo;
import com.easonhotel.dao.web.service.IUserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class LoginInterceptor implements AsyncHandlerInterceptor {
    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler.getClass() == HandlerMethod.class) {
            HandlerMethod method = (HandlerMethod) handler;
            LoginRequired loginRequired = method.getMethodAnnotation(LoginRequired.class);
            if (loginRequired != null) {
                String memberId = request.getHeader("__memberId");
                String token = request.getHeader("__token");
                if (StringUtils.isNotBlank(memberId) && StringUtils.isNotBlank(token)) {
                    UserInfo userInfo = userInfoService.getById(memberId);
                    if (userInfo != null && token.equals(userInfo.getToken())) {
                        return true;
                    }
                }
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter printWriter = response.getWriter();
                ResData resData = ResData.create(401, "拒绝访问");
                printWriter.write(new ObjectMapper().writeValueAsString(resData));
                printWriter.flush();
                printWriter.close();
                return false;
            }
        }
        return true;
    }
}

package com.zoley.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoley.common.Result;
import com.zoley.common.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 类 名: AuthInterceptor
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月01日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String auth = request.getHeader("Authorization");
    if (auth == null || !auth.startsWith("Bearer ")) {
      // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=utf-8");
      Result<Object> error = Result.error(ResultCode.UNAUTHORIZED);
      response.getWriter().write(new ObjectMapper().writeValueAsString(error));
      return false;
    }
    System.out.println("token = " + auth);
    return true;
  }
}

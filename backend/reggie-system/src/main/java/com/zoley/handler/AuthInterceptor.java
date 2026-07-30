package com.zoley.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoley.common.context.BaseContext;
import com.zoley.common.result.Result;
import com.zoley.common.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String auth = request.getHeader("Authorization");
    String token = null;
    if (auth != null && auth.startsWith("Bearer ")) {
      token=auth.substring("Bearer ".length()).trim();
    }
    if(token == null || token.isEmpty()){
      String paramToken = request.getParameter("token");
      if (paramToken != null && paramToken.startsWith("Bearer ")) {
        token=paramToken.substring("Bearer ".length()).trim();
        if(token.isEmpty()){
          token=null;
        }
      }
    }
    if (token == null) {
      // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=utf-8");
      Result<Object> error = Result.error(ResultCode.CODE_401);
      response.getWriter().write(new ObjectMapper().writeValueAsString(error));
      return false;
    }
    try {
      // 如果是 JWT，请替换为 JwtUtil.parseToken(token) 获取 userId
      Long currentId =Long.parseLong(token);
      BaseContext.setCurrentId(currentId);
      log.info("token = {}", token);
      return true;
    } catch (NumberFormatException e) {
      response.setContentType("application/json;charset=utf-8");
      Result<Object> error = Result.error(ResultCode.CODE_401);
      response.getWriter().write(new ObjectMapper().writeValueAsString(error));
      return false;
    }
  }
}

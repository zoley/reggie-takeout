package com.zoley.handler;

import com.zoley.common.result.Result;
import com.zoley.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 类 名: GlobalExceptionHandler
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月16日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  /**
   * 全局异常
   */
  @ExceptionHandler(Exception.class)
  public Result<Void> handleException(Exception e) {
    log.error("全局异常处理: {}", e.getMessage(), e);
    return Result.error(ResultCode.CODE_500);
  }

  /**
   * 唯一性重复
   */
  @ExceptionHandler(DuplicateKeyException.class)
  public Result<String> handleDuplicateKey(DuplicateKeyException e) {
    log.error("全局异常处理", e);
    String message = e.getMessage();
    if (message.contains("Duplicate entry")) {
      // 示例：Duplicate entry 'admin' for key 'user.username'
      String s = message.split("'")[1];
      return Result.error("【" + s + "】已存在，请重新输入");
    }
    return Result.error("数据重复，请检查后再试");
  }

  /**
   * 参数异常
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result<String> handleValidationException(MethodArgumentNotValidException e) {
    List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
    String msg = ResultCode.CODE_422.getMsg();
    if (!allErrors.isEmpty()) {
      msg = allErrors.get(0).getDefaultMessage();
      if (!StringUtils.hasText(msg)) {
        msg = ResultCode.CODE_422.getMsg();
      }
    }
    return Result.error(msg);
  }


}

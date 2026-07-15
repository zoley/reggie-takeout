package com.zoley.common.result;

import lombok.Getter;

/**
 * 类 名: Result
 * 描 述: 统一接口响应结果
 * 作 者: 主成睿
 * 创 建：2026年06月29日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Getter
public class Result<T> {
  private final Integer code;
  private final String msg;
  private final T data;

  private Result(Integer code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public static <T> Result<T> success() {
    return build(ResultCode.CODE_200, null);
  }

  public static <T> Result<T> success(String msg) {
    return build(ResultCode.CODE_200.getCode(), msg, null);
  }

  public static <T> Result<T> success(T data) {
    return build(ResultCode.CODE_200, data);
  }

  public static <T> Result<T> success(String msg, T data) {
    return build(ResultCode.CODE_200.getCode(), msg, data);
  }

  public static <T> Result<T> error() {
    return build(ResultCode.CODE_500, null);
  }

  public static <T> Result<T> error(String msg) {
    return build(ResultCode.CODE_500.getCode(), msg, null);
  }

  public static <T> Result<T> error(ResultCode resultCode) {
    return build(resultCode, null);
  }

  private static <T> Result<T> build(ResultCode resultCode, T data) {
    return build(resultCode.getCode(), resultCode.getMsg(), data);
  }

  private static <T> Result<T> build(Integer code, String msg, T data) {
    return new Result<>(code, msg, data);
  }

}

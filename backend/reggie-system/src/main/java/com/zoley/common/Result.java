package com.zoley.common;

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
  private Integer code;
  private String msg;
  private T data;

  private Result(Integer code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public static <T> Result<T> success() {
    return build(ResultCode.SUCCESS, null);
  }

  public static <T> Result<T> success(String msg) {
    return build(ResultCode.SUCCESS.getCode(), msg, null);
  }

  public static <T> Result<T> success(T data) {
    return build(ResultCode.SUCCESS, data);
  }

  public static <T> Result<T> success(String msg, T data) {
    return build(ResultCode.SUCCESS.getCode(), msg, data);
  }

  public static <T> Result<T> error() {
    return build(ResultCode.FAIL, null);
  }

  public static <T> Result<T> error(String msg) {
    return build(ResultCode.FAIL.getCode(), msg, null);
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

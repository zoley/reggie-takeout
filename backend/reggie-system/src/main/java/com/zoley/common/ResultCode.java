package com.zoley.common;

import lombok.Getter;

/**
 * 类 名: ResultCode
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年06月29日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Getter
public enum ResultCode {
  SUCCESS(200, "操作成功"),
  FAIL(500, "操作失败"),
  UNAUTHORIZED(401, "未授权"),
  FORBIDDEN(403, "禁止访问");
  private final int code;
  private final String msg;

  ResultCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

}

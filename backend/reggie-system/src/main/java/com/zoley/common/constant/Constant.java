package com.zoley.common.constant;

/**
 * 类 名: Constant
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月01日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
public final class Constant {
  public static final String BASE_PATH = "/api/v1";
  public static final String[] AUTH_EXCLUDE_PATHS = {
      "/api/v1/employee/login"
  };
  private Constant() {
  }

}
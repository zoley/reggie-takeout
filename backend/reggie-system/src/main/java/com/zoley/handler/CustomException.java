package com.zoley.handler;

/**
 * 类 名: CustomException
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月30日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
public class CustomException extends RuntimeException {
  public CustomException(String message) {
    super(message);
  }
}

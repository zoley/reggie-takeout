package com.zoley.common;

import com.zoley.entity.Employee;

/**
 * 类 名: BaseContext
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月14日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
public class BaseContext {
  private static final ThreadLocal<Long> contextHolder = new ThreadLocal<Long>();
  public static void setCurrentId(Long id) {
    contextHolder.set(id);
  }
  public  static Long getCurrentId() {
    return contextHolder.get();
  }
}

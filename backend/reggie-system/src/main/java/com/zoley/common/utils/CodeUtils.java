package com.zoley.common.utils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * 类 名: Utils
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年06月29日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
public final class CodeUtils {
  private CodeUtils() {}
  private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

  public static String codeGenerate() {
    Long id = IdWorker.getId();
    return encodeBase62(id);
  }

  private static String encodeBase62(Long num) {
    if (num == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    while (num > 0) {
      int index = (int) (num % 62);
      sb.append(CHARS[index]);
      num = num / 62;
    }
    return sb.reverse().toString();
  }
}

package com.zoley.entity;

import lombok.Data;

/**
 * 类 名: PageDTO
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年06月29日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class PaginationDTO {
  private int current; // 当前页码
  private int pageSize; // 每页大小
  private int total; // 总记录数
}

package com.zoley.entity;

import lombok.Data;

/**
 * 类 名: EmployeeSearch
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年06月29日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class EmployeeSearch  extends PaginationDTO {
  private String name;
  private String userName;
}

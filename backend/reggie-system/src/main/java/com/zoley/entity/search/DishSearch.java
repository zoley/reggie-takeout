package com.zoley.entity.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类 名: DishSearch
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月28日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DishSearch extends PaginationDTO{
  private String name;
  private Long categoryId;
  private Integer status;
}

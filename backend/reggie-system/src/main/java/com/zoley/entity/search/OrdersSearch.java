package com.zoley.entity.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类 名: OrdersSearch
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年08月19日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrdersSearch extends PaginationDTO{
  private String number;
  private String phone;
  // 1=待付款 2=待收货 3=待评价 4=退款/售后 0=全部
  private Integer status;
}

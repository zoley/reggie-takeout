package com.zoley.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 类 名: OrderDetail
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年08月19日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@TableName
@Data
public class OrderDetail {
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long id;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long orderId;
  private String name;
  private String image;
  private BigDecimal amount;
  private Integer number;
  private Long setmealId;
  private Long dishId;
  private String dishFlavor;
}

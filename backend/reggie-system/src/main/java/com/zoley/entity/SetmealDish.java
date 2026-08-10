package com.zoley.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类 名: SetmealDish
 * 描 述: 套餐菜品
 * 作 者: 主成睿
 * 创 建：2026年08月10日
 * 版 本：v1.0.0
 *
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class SetmealDish{
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long id;
  private String name;
  private double price;
  private Integer copies;
  @JsonFormat(shape =  JsonFormat.Shape.STRING)
  private Long setmealId;
  @JsonFormat(shape =  JsonFormat.Shape.STRING)
  private Long dishId;
}

package com.zoley.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类 名: DishFlavor
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年08月04日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DishFlavor extends Base{
  private String name;
  private String value;
  @JsonFormat(shape =  JsonFormat.Shape.STRING)
  private Long dishId;
}

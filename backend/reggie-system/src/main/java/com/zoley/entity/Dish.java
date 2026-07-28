package com.zoley.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类 名: Dish
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月28日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dish")
public class Dish  extends Base{
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long id;
  private String code;
  private String name;
  private Integer sort;
  private Double price;
  private Integer image;
  private Integer description;
  private Integer status;
  private Long categoryId;

}

package com.zoley.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类 名: Setmeal
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月29日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Setmeal  extends Base{
  private String name;
  private String code;
  private Double price;
  private String image;
  private String description;
  private Integer status;
  @JsonFormat(shape =  JsonFormat.Shape.STRING)
  private Long categoryId;
}

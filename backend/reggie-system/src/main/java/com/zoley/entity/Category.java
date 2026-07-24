package com.zoley.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类 名: Category
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月24日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@EqualsAndHashCode(callSuper = true)
@TableName("category")
@Data
public class Category extends Base {
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long id;

  private String name;

  private Integer sort;

  private Integer type;

}

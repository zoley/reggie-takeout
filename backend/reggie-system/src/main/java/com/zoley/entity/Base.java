package com.zoley.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类 名: Base
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月24日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Data
public class Base {
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long id;
  /**
   * 创建时间（插入时自动填充）
   */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime createTime;

  /**
   * 更新时间（插入、更新时自动填充）
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private LocalDateTime updateTime;

  /**
   * 创建人ID（插入时自动填充）
   */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long createUser;

  /**
   * 修改人ID（插入、更新时自动填充）
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long updateUser;

}

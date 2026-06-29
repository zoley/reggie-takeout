package com.zoley.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类 名: Employee
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年06月15日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@TableName("employee")
@Data
public class Employee {
  private Long id;
  /**
   * 姓名
   */
  private String name;

  /**
   * 登录账号
   */
  private String userName;

  /**
   * 密码（MD5加密存储）
   */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  /**
   * 手机号
   */
  private String phone;

  /**
   * 性别
   */
  private String sex;

  /**
   * 身份证号
   */
  private String idNumber;

  /**
   * 状态 0:禁用 1:正常
   */
  private Integer status;

   /**
    * 创建时间（插入时自动填充）
    */
   @TableField(fill = FieldFill.INSERT)
   private LocalDateTime createTime;

   /**
    * 更新时间（插入、更新时自动填充）
    */
   @TableField(fill = FieldFill.INSERT_UPDATE)
   private LocalDateTime updateTime;

   /**
    * 创建人ID（插入时自动填充）
    */
   @TableField(fill = FieldFill.INSERT)
   private Long createUser;

   /**
    * 修改人ID（插入、更新时自动填充）
    */
   @TableField(fill = FieldFill.INSERT_UPDATE)
   private Long updateUser;
}
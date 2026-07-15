package com.zoley.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long id;
  /**
   * 姓名
   */
  @NotBlank(message = "姓名不能为空")
  private String name;

  /**
   * 登录账号
   */
  @NotBlank(message = "登录账号不能为空")
  private String userName;

  /**
   * 密码（MD5加密存储）
   */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  /**
   * 手机号
   */
  @NotBlank(message = "手机号不能为空")
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
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
  private LocalDateTime createTime;

  /**
   * 更新时间（插入、更新时自动填充）
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
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

  /**
   * 删除标志  0:未删除 1:已删除
   */
  // @TableLogic(value="0",delval="1")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @TableField(fill = FieldFill.INSERT)
  private Integer deleted;
}
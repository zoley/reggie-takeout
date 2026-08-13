package com.zoley.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 重置密码请求 DTO
 */
@Data
public class ResetPasswordDTO {

  @NotNull(message = "员工ID不能为空")
  private Long id;

  @NotBlank(message = "新密码不能为空")
  private String password;

  @NotBlank(message = "确认密码不能为空")
  private String confirmPassword;
}

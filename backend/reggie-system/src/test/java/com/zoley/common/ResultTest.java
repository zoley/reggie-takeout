package com.zoley.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultTest {

  @Test
  void successWithDataUsesDefaultSuccessCodeAndMessage() {
    Result<Integer> result = Result.success(1);

    assertThat(result.getCode()).isEqualTo(ResultCode.SUCCESS.getCode());
    assertThat(result.getMsg()).isEqualTo(ResultCode.SUCCESS.getMsg());
    assertThat(result.getData()).isEqualTo(1);
  }

  @Test
  void successWithStringDataUsesCustomMessageAndDataOverload() {
    Result<String> result = Result.success("查询成功", "ok");

    assertThat(result.getCode()).isEqualTo(ResultCode.SUCCESS.getCode());
    assertThat(result.getMsg()).isEqualTo("查询成功");
    assertThat(result.getData()).isEqualTo("ok");
  }

  @Test
  void errorWithMessageUsesDefaultFailCodeAndCustomMessage() {
    Result<Void> result = Result.error("参数错误");

    assertThat(result.getCode()).isEqualTo(ResultCode.FAIL.getCode());
    assertThat(result.getMsg()).isEqualTo("参数错误");
    assertThat(result.getData()).isNull();
  }

  @Test
  void errorWithResultCodeUsesEnumCodeAndMessage() {
    Result<Void> result = Result.error(ResultCode.UNAUTHORIZED);

    assertThat(result.getCode()).isEqualTo(ResultCode.UNAUTHORIZED.getCode());
    assertThat(result.getMsg()).isEqualTo(ResultCode.UNAUTHORIZED.getMsg());
    assertThat(result.getData()).isNull();
  }
}

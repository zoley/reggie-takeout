package com.zoley.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 类 名: Order
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年08月18日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Data
@TableName("orders")
public class Orders {
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long id;
  //订单号
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long number;
  // 1=待付款 2=待收货 3=待评价 4=退款/售后 0=全部
  private Integer status;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long userId;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Long addressBookId;
  // 订单时间
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
  private LocalDateTime orderTime;
  // 付款时间
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
  private LocalDateTime checkoutTime;
  // 1=微信 2=支付宝
  private Integer payMethod;
  // 1=成功 0=失败
  private Integer payStatus;

  private BigDecimal amount;

  private String remark;

  private String phone;

  private String address;

  private String userName;

  private String consignee;

}

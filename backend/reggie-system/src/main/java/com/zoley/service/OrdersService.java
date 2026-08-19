package com.zoley.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoley.dto.OrdersDTO;
import com.zoley.entity.Orders;

/**
 * 类 名: OrdersService
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年08月19日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
public interface OrdersService extends IService<Orders> {
  OrdersDTO getOrderAndDetailById(Long id);
}

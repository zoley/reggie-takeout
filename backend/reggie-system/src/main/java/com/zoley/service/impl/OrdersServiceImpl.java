package com.zoley.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoley.common.result.Result;
import com.zoley.dto.OrdersDTO;
import com.zoley.entity.OrderDetail;
import com.zoley.entity.Orders;
import com.zoley.handler.CustomException;
import com.zoley.mapper.OrdersMapper;
import com.zoley.service.OrderDetailService;
import com.zoley.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类 名: OrdersServiceImpl
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年08月19日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Service
@RequiredArgsConstructor
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper,Orders> implements OrdersService {
  private final OrderDetailService orderDetailService;
  @Override
  public OrdersDTO getOrderAndDetailById(Long id) {
    if(id==null){
      throw new CustomException("ID不能为空");
    }
    Orders orders = super.getById(id);
    OrdersDTO ordersDTO = new OrdersDTO();
    BeanUtils.copyProperties(orders, ordersDTO);
    LambdaQueryWrapper<OrderDetail> detailLambdaQueryWrapper = new LambdaQueryWrapper<>();
    detailLambdaQueryWrapper.eq(OrderDetail::getOrderId,id);
    List<OrderDetail> details = orderDetailService.list(detailLambdaQueryWrapper);
    ordersDTO.setOrderDetails(details);
    return ordersDTO;
  }
}






















package com.zoley.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoley.common.result.Result;
import com.zoley.dto.OrdersDTO;
import com.zoley.entity.OrderDetail;
import com.zoley.entity.Orders;
import com.zoley.entity.search.OrdersSearch;
import com.zoley.handler.CustomException;
import com.zoley.service.OrderDetailService;
import com.zoley.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zoley.common.constant.Constant.BASE_PATH;

/**
 * 类 名: OrdersController
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年08月19日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_PATH + "/order")
public class OrdersController {
  private final OrdersService ordersService;

  @PostMapping("/listByPage")
  public Result<Page<Orders>> listByPage(@RequestBody OrdersSearch ordersSearch) {
    Page<Orders> ordersPage = new Page<>();
    ordersPage.setCurrent(ordersSearch.getCurrent());
    ordersPage.setSize(ordersSearch.getPageSize());
    LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
    ordersLambdaQueryWrapper.eq(StringUtils.hasText(ordersSearch.getNumber()), Orders::getNumber, ordersSearch.getNumber());
    ordersLambdaQueryWrapper.eq(ordersSearch.getStatus() != null, Orders::getStatus, ordersSearch.getStatus());
    ordersLambdaQueryWrapper.eq(StringUtils.hasText(ordersSearch.getPhone()), Orders::getPhone, ordersSearch.getPhone());
    ordersLambdaQueryWrapper.orderByDesc(Orders::getCheckoutTime);
    Page<Orders> pageResult = ordersService.page(ordersPage, ordersLambdaQueryWrapper);
    return Result.success(pageResult);
  }

  @GetMapping("/getById")
  public Result<OrdersDTO> getById(@RequestParam("id") Long id) {
    OrdersDTO detail = ordersService.getOrderAndDetailById(id);
    return Result.success(detail);
  }

  @GetMapping("/cancel/{id}")
  public Result<String> cancel(@PathVariable Long id) {
    Orders orders = ordersService.getById(id);
    orders.setStatus(6);
    boolean b = ordersService.updateById(orders);
    return b?Result.success("取消成功"):Result.error("取消失败");
  }

  @GetMapping("/done/{id}")
  public Result<String> done(@PathVariable Long id) {
    Orders orders = ordersService.getById(id);
    orders.setStatus(5);
    boolean b = ordersService.updateById(orders);
    return b?Result.success("设置成功"):Result.error("设置失败");
  }



























}










































package com.zoley.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoley.entity.OrderDetail;
import com.zoley.mapper.OrderDetailMapper;
import com.zoley.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * 类 名: OrderDetailServiceImpl
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年08月19日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}

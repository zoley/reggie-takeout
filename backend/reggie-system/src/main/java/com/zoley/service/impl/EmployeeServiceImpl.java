package com.zoley.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoley.entity.Employee;
import com.zoley.mapper.EmployeeMapper;
import com.zoley.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * 类 名: EmployeeServiceImpl
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年06月15日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

package com.zoley.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoley.common.Result;
import com.zoley.entity.Employee;
import com.zoley.entity.EmployeeSearch;
import com.zoley.entity.PaginationDTO;
import com.zoley.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.zoley.common.Utils.BASE_PATH;

/**
 * 类 名: EmployeeController
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年06月15日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@RestController
@RequestMapping(BASE_PATH + "/employee")
public class EmployeeController {
  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping("/listByPage")
  public Result<Page<Employee>> getListByPage(@RequestBody EmployeeSearch employeeSearch) {
    String name = employeeSearch.getName();
    String userName = employeeSearch.getUserName();
    Page<Employee> page = new Page<>(employeeSearch.getCurrent(), employeeSearch.getPageSize());
    LambdaQueryWrapper<Employee> eqw = new LambdaQueryWrapper<>();
    eqw.like(StringUtils.hasText(name), Employee::getName, name);
    eqw.like(userName != null, Employee::getUserName, userName);
    Page<Employee> pageResult = employeeService.page(page, eqw);
    return Result.success("查询成功", pageResult);
  }
}




















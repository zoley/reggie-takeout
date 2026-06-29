package com.zoley.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoley.common.Result;
import com.zoley.entity.Employee;
import com.zoley.entity.EmployeeSearch;
import com.zoley.entity.PaginationDTO;
import com.zoley.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
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

  /**
    * 描 述： 分页查询员工列表
    * 作 者： 主成睿
    * 历 史： (版本) 作者 时间 注释
   */
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

  /**
    * 描 述： 员工登录
    * 作 者： 主成睿
    * 历 史： (版本) 作者 时间 注释
    * @param employee 员工登录信息
    * @return 登录成功后的员工信息
    */
  @PostMapping("/login")
  public Result<Employee> login(@RequestBody Employee employee) {
    if(!StringUtils.hasText(employee.getUserName())){
      return Result.error("用户名不能为空");
    }
    if(!StringUtils.hasText(employee.getPassword())){
      return Result.error("密码不能为空");
    }
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Employee::getUserName, employee.getUserName());
    Employee findEmployee = employeeService.getOne(queryWrapper);
    if(findEmployee == null){
      return Result.error("用户名或密码错误");
    }
    String md5Password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
    if(!md5Password.equals(findEmployee.getPassword())){
      return Result.error("用户名或密码错误");
    }
    if(findEmployee.getStatus() == 0){
      return Result.error("员工已被禁用,请联系管理员");
    }
    return Result.success("登录成功", findEmployee);
  }
}




















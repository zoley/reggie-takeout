package com.zoley.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoley.common.result.Result;
import com.zoley.common.utils.CodeUtils;
import com.zoley.entity.Employee;
import com.zoley.entity.EmployeeSearch;
import com.zoley.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.zoley.common.constant.Constant.BASE_PATH;


/**
 * 类 名: EmployeeController
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年06月15日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Slf4j
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
   *
   * @param employee 员工登录信息
   * @return 登录成功后的员工信息
   */
  @PostMapping("/login")
  public Result<Employee> login(@RequestBody Employee employee) {
    if (!StringUtils.hasText(employee.getUserName())) {
      return Result.error("用户名不能为空");
    }
    if (!StringUtils.hasText(employee.getPassword())) {
      return Result.error("密码不能为空");
    }
    LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Employee::getUserName, employee.getUserName());
    Employee findEmployee = employeeService.getOne(queryWrapper);
    if (findEmployee == null) {
      return Result.error("用户名或密码错误");
    }
    String md5Password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
    if (!md5Password.equals(findEmployee.getPassword())) {
      return Result.error("用户名或密码错误");
    }
    if (findEmployee.getStatus() == 0) {
      return Result.error("员工已被禁用,请联系管理员");
    }
    return Result.success("登录成功", findEmployee);
  }

  /**
   * 描 述： 创建员工
   * 作 者： 主成睿
   * 历 史： (版本) 作者 时间 注释
   *
   * @param employee 员工信息
   * @return 创建成功后的员工信息
   */

  @PostMapping("/create")
  public Result<Employee> create(@Validated @RequestBody Employee employee) {
    String code = CodeUtils.codeGenerate();
    log.info("创建员工ID: {}", code);
    String newPassword = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
    employee.setPassword(newPassword);
    boolean isOk = employeeService.save(employee);
    if (isOk) {
      return Result.success("创建成功", employee);
    }
    return Result.error("创建失败");

  }

  @PostMapping("/update")
  public Result<Employee> update(@RequestBody Employee employee) {
    boolean isOk = employeeService.updateById(employee);
    if (isOk) {
      return Result.success("更新成功", employee);
    }
    return Result.error("更新失败");
  }

  @GetMapping("/enabledStatus")
  public Result<Employee> enabledStatus(@RequestParam Long id) {
    Employee employee = employeeService.getById(id);
    if (employee == null) {
      return Result.error("员工不存在");
    }
    employee.setStatus(1);
    boolean isOk = employeeService.updateById(employee);
    if (isOk) {
      return Result.success("启用成功");
    }
    return Result.error("启用失败");
  }
  @GetMapping("/disabledStatus")
  public Result<Employee> disabledStatus(@RequestParam Long id) {
    Employee employee = employeeService.getById(id);
    if (employee == null) {
      return Result.error("员工不存在");
    }
    employee.setStatus(0);
    boolean isOk = employeeService.updateById(employee);
    if (isOk) {
      return Result.success("禁用员工成功");
    }
    return Result.error("禁用员工失败");
  }

  @DeleteMapping("/batchDelete")
  public Result<Employee> deleteBatch(@RequestBody List<Long> ids) {
    boolean isOk = employeeService.removeByIds(ids);
    if (isOk) {
      return Result.success("删除成功");
    }
    return Result.error("删除失败");
  }






}




















package com.zoley.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoley.common.result.Result;
import com.zoley.dto.DishDTO;
import com.zoley.entity.Category;
import com.zoley.entity.Dish;
import com.zoley.entity.Employee;
import com.zoley.entity.search.DishSearch;
import com.zoley.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zoley.common.constant.Constant.BASE_PATH;

/**
 * 类 名: DishController
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月28日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_PATH+"/dish")
public class DishController {
  private final DishService dishService;

  @PostMapping("/listByPage")
  public Result<Page<Dish>> listByPage(@RequestBody DishSearch dishSearch) {
    String name = dishSearch.getName();
    Long categoryId = dishSearch.getCategoryId();
    Integer status = dishSearch.getStatus();
    LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
    dishLambdaQueryWrapper.like(StringUtils.hasText(name), Dish::getName, name);
    dishLambdaQueryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);
    dishLambdaQueryWrapper.eq(status != null, Dish::getStatus, status);
    dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    Page<Dish> page = new Page<>(dishSearch.getCurrent(), dishSearch.getPageSize());
    Page<Dish> pageResult = dishService.page(page, dishLambdaQueryWrapper);
    return Result.success(pageResult);
  }

  @PostMapping("/create")
  public Result<Dish> create(@RequestBody DishDTO dishDTO) {
    boolean isOk = dishService.saveDishAndFlavor(dishDTO);
    if (isOk) {
      return Result.success("创建成功", dishDTO);
    }
    return Result.error("创建失败");
  }

  @PutMapping("/update")
  public Result<Dish> update(@RequestBody DishDTO dishDTO) {
    boolean isOk = dishService.updateDishAndFlavor(dishDTO);
    if (isOk) {
      return Result.success("更新成功", dishDTO);
    }
    return Result.error("更新失败");
  }

  @GetMapping("/getById")
  public Result<DishDTO> getById(@RequestParam Long id) {
    DishDTO dishDTO = dishService.getDishAndFlavorById(id);
    if (dishDTO == null) {
      return Result.error("菜品信息不存在");
    }
    return Result.success(dishDTO);
  }
  @DeleteMapping("/delete/{id}")
  public Result<String> delete(@PathVariable Long id) {
    boolean isOk  = dishService.removeById(id);
    if (isOk) {
      return Result.success("删除成功");
    }
    return Result.error("删除失败");
  }
  @PostMapping("/batchDelete")
  public Result<String> batchDelete(@RequestBody List<Long> ids) {
    boolean isOk  = dishService.removeByIds(ids);
    if (isOk) {
      return Result.success("删除成功");
    }
    return Result.error("删除失败");
  }
  @GetMapping("/enabledStatus")
  public Result<String> enabledStatus(@RequestParam Long id) {
    DishDTO dishDTO =  dishService.getDishAndFlavorById(id);
    if (dishDTO == null) {
      return Result.error("菜品信息不存在");
    }
    dishDTO.setStatus(1);
    boolean isOk =dishService.updateById(dishDTO);
    if (isOk) {
      return Result.success("启用成功");
    }
    return Result.error("启用失败");
  }
  @GetMapping("/disabledStatus")
  public Result<String> disabledStatus(@RequestParam Long id) {
    DishDTO dishDTO =  dishService.getDishAndFlavorById(id);
    if (dishDTO == null) {
      return Result.error("菜品信息不存在");
    }
    dishDTO.setStatus(0);
    boolean isOk =dishService.updateById(dishDTO);
    if (isOk) {
      return Result.success("停用成功");
    }
    return Result.error("停用失败");
  }
}




















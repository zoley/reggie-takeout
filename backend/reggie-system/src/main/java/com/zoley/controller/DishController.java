package com.zoley.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoley.common.result.Result;
import com.zoley.entity.Dish;
import com.zoley.entity.search.DishSearch;
import com.zoley.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    Page<Dish> page = new Page<>(dishSearch.getCurrent(), dishSearch.getPageSize());
    Page<Dish> pageResult = dishService.page(page, dishLambdaQueryWrapper);
    return Result.success(pageResult);
  }
}




















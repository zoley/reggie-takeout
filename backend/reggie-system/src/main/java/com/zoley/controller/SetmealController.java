package com.zoley.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoley.common.result.Result;
import com.zoley.entity.Dish;
import com.zoley.entity.Setmeal;
import com.zoley.entity.search.SetmealSearch;
import com.zoley.service.DishService;
import com.zoley.service.SetmealService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.zoley.common.constant.Constant.BASE_PATH;

/**
 * 类 名: SetmealController
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月30日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_PATH + "/setmeal")
public class SetmealController {
  private final SetmealService setmealService;

  @PostMapping("/listByPage")
  public Result<Page<Setmeal>> listByPage(@RequestBody SetmealSearch setmealSearch) {
    Page<Setmeal> setmealPage = new Page<>(setmealSearch.getCurrent(), setmealSearch.getPageSize());
    LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
    setmealLambdaQueryWrapper.like(StringUtils.hasText(setmealSearch.getName()), Setmeal::getName, setmealSearch.getName());
    setmealLambdaQueryWrapper.eq(setmealSearch.getCategoryId() != null, Setmeal::getCategoryId, setmealSearch.getCategoryId());
    setmealLambdaQueryWrapper.eq(setmealSearch.getStatus() != null, Setmeal::getStatus, setmealSearch.getStatus());
    setmealLambdaQueryWrapper.orderByAsc(Setmeal::getSort).orderByAsc(Setmeal::getUpdateTime);
    Page<Setmeal> pageResult = setmealService.page(setmealPage, setmealLambdaQueryWrapper);
    return Result.success(pageResult);
  }
}








































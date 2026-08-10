package com.zoley.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoley.common.result.Result;
import com.zoley.dto.SetmealDTO;
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
    setmealLambdaQueryWrapper.orderByAsc(Setmeal::getSort).orderByDesc(Setmeal::getUpdateTime);
    Page<Setmeal> pageResult = setmealService.page(setmealPage, setmealLambdaQueryWrapper);
    return Result.success(pageResult);
  }

  @PostMapping("/create")
  public Result<SetmealDTO> create(@RequestBody SetmealDTO setmealDTO) {
    boolean isOk = setmealService.saveSetmealAndSeries(setmealDTO);
    return isOk ? Result.success(setmealDTO) : Result.error("新增失败");
  }
  @PutMapping("/update")
  public Result<SetmealDTO> update(@RequestBody SetmealDTO setmealDTO) {
    boolean isOk = setmealService.updateSetmealAndSeries(setmealDTO);
    return isOk ? Result.success(setmealDTO) : Result.error("新增失败");
  }

  @GetMapping("/getById")
  public Result<SetmealDTO> getById(@RequestParam Long id) {
    SetmealDTO setmealDTO = setmealService.getSetmealAndSeriesById(id);
    return setmealDTO!=null ? Result.success(setmealDTO) : Result.error("查询失败");
  }

  @GetMapping("/enabledStatus")
  public Result<String> enabledStatus(@RequestParam Long id) {
    Setmeal setmeal = setmealService.getById(id);
    if (setmeal == null) {
      return Result.error("套餐不存在");
    }
    setmeal.setStatus(1);
    boolean isOk = setmealService.updateById(setmeal);
    return isOk ? Result.success("起售成功") : Result.error("起售失败");
  }


  @GetMapping("/disabledStatus")
  public Result<String> disabledStatus(@RequestParam Long id) {
    Setmeal setmeal = setmealService.getById(id);
    if (setmeal == null) {
      return Result.error("套餐不存在");
    }
    setmeal.setStatus(0);
    boolean isOk = setmealService.updateById(setmeal);
    return isOk ? Result.success("停售成功") : Result.error("停售失败");
  }

  @DeleteMapping("/delete/{id}")
  public Result<String> delete(@PathVariable Long id) {
    return setmealService.removeById(id) ? Result.success("删除成功") : Result.error("删除失败");
  }


  @PostMapping("/deleteBatch")
  public Result<String> deleteBatch(@RequestBody List<Long> ids) {
    return setmealService.removeByIds(ids)? Result.success("删除成功") : Result.error("删除失败");
  }




}








































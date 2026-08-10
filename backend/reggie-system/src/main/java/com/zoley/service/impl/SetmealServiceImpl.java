package com.zoley.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoley.dto.SetmealDTO;
import com.zoley.entity.Category;
import com.zoley.entity.Setmeal;
import com.zoley.entity.SetmealDish;
import com.zoley.handler.CustomException;
import com.zoley.mapper.SetmealMapper;
import com.zoley.service.CategoryService;
import com.zoley.service.SetmealDishService;
import com.zoley.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类 名: SetmealServiceImpl
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月30日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
  private final SetmealDishService setmealDishService;
  private final CategoryService categoryService;

  public SetmealServiceImpl(SetmealDishService setmealDishService, CategoryService categoryService) {
    this.setmealDishService = setmealDishService;
    this.categoryService = categoryService;
  }

  @Transactional
  @Override
  public boolean saveSetmealAndSeries(SetmealDTO setmealDTO) {
    boolean save = super.save(setmealDTO);
    if (!save) {
      throw new CustomException("保存套餐失败");
    }
    List<SetmealDish> series = setmealDTO.getSeries();
    Long id = setmealDTO.getId();
    // for (SetmealDish setmealDish : series) {
    //   setmealDish.setId(id);
    // }
    if (series != null && !series.isEmpty()) {
      series.forEach(setmealDish -> {
        setmealDish.setSetmealId(id);
      });
      if (!setmealDishService.saveBatch(series)) throw new CustomException("保存套餐关联菜品失败");
    }

    return true;
  }

  @Transactional
  @Override
  public boolean updateSetmealAndSeries(SetmealDTO setmealDTO) {
    boolean b = super.updateById(setmealDTO);
    if (!b) {
      throw new CustomException("更新套餐失败");
    }
    List<SetmealDish> series = setmealDTO.getSeries();
    Long id = setmealDTO.getId();
    LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SetmealDish::getSetmealId, id);
    setmealDishService.remove(queryWrapper);
    if (series != null && !series.isEmpty()) {
      for (SetmealDish setmealDish : series) {
        setmealDish.setSetmealId(id);
      }
      if (!setmealDishService.saveBatch(series)) {
        log.error("保存套餐关联菜品失败, setmealId={}",id);
        throw new CustomException("更新套餐关联菜品失败");
      }
    }
    return true;
  }

  @Override
  public SetmealDTO getSetmealAndSeriesById(Long id) {
    SetmealDTO setmealDTO = new SetmealDTO();
    Setmeal setmeal = super.getById(id);
    BeanUtils.copyProperties(setmeal, setmealDTO);
    LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
    setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
    List<SetmealDish> list = setmealDishService.list(setmealDishLambdaQueryWrapper);
    setmealDTO.setSeries(list);
    // 前端列表需要返回分类名称？
    Category category = categoryService.getById(setmeal.getCategoryId());
    if (category != null) {
      setmealDTO.setCategoryName(category.getName());
    }
    return setmealDTO;
  }
}




































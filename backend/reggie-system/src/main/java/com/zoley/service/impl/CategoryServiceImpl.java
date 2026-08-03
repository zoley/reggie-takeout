package com.zoley.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoley.entity.Category;
import com.zoley.entity.Dish;
import com.zoley.entity.Setmeal;
import com.zoley.handler.CustomException;
import com.zoley.mapper.CategoryMapper;
import com.zoley.mapper.DishMapper;
import com.zoley.mapper.SetmealMapper;
import com.zoley.service.CategoryService;
import com.zoley.service.DishService;
import com.zoley.service.SetmealService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类 名: CategoryServiceImpl
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月24日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


  private final DishMapper dishMapper;
  private final SetmealMapper setmealMapper;

  @Override
  public boolean customRemoveById(Long id) {
    LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
    dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
    long count = dishMapper.selectCount(dishLambdaQueryWrapper);
    if(count > 0) {
      throw new CustomException("当前分类关联了菜品，不能删除");
    }
    LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
    setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
    long count2 = setmealMapper.selectCount(setmealLambdaQueryWrapper);
    if(count2 > 0) {
      throw new CustomException("当前分类关联了套餐，不能删除");
    }
    return super.removeById(id);
  }
}

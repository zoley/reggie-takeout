package com.zoley.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoley.dto.DishDTO;
import com.zoley.entity.Category;
import com.zoley.entity.Dish;
import com.zoley.entity.DishFlavor;
import com.zoley.mapper.DishMapper;
import com.zoley.service.CategoryService;
import com.zoley.service.DishFlavorService;
import com.zoley.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类 名: DishServiceImpl
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月28日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Service
@RequiredArgsConstructor
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
  private final DishFlavorService dishFlavorService;
  private final CategoryService categoryService;
  @Transactional
  @Override
  public boolean saveDishAndFlavor(DishDTO dishDTO) {
    super.save(dishDTO);
    Long dishId = dishDTO.getId();
    List<DishFlavor> flavors = dishDTO.getFlavors();
    // 优
    for (DishFlavor dishFlavor : flavors) {
      dishFlavor.setDishId(dishId);
    }
    /*
     中--------
     flavors.forEach(x->{
       x.setDishId(dishId);
     });
     劣--------
     List<DishFlavor> newFlavor = flavors.stream().map(x -> {
       x.setDishId(dishId);
       return x;
     }).collect(Collectors.toList());
    */
    return dishFlavorService.saveBatch(flavors);
  }

  @Transactional
  @Override
  public boolean updateDishAndFlavor(DishDTO dishDTO) {
    super.updateById(dishDTO);
    Long dishId = dishDTO.getId();
    // 先删除后插入（按 dishId 删除该菜品原有口味）
    LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
    dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
    dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
    List<DishFlavor> flavors = dishDTO.getFlavors();
    for (DishFlavor dishFlavor : flavors) {
      dishFlavor.setDishId(dishId);
    }
    return dishFlavorService.saveBatch(flavors);
  }

  @Override
  public DishDTO getDishAndFlavorById(Long id) {
    Dish dish = super.getById(id);
    DishDTO dishDTO = new DishDTO();
    BeanUtils.copyProperties(dish, dishDTO);
    LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
    dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
    List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
    dishDTO.setFlavors(dishFlavors);
    Category category = categoryService.getById(dish.getCategoryId());
    if(category != null){
      dishDTO.setCategoryName(category.getName());
    }
    return dishDTO;
  }






}

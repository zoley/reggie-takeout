package com.zoley.dto;

import com.zoley.entity.Dish;
import com.zoley.entity.DishFlavor;
import com.zoley.entity.search.PaginationDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 类 名: DishDTO
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年08月03日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DishDTO extends Dish {
  private List<DishFlavor> flavors;
  private String  categoryName;
}

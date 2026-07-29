package com.zoley.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoley.entity.Category;

/**
 * 类 名: CategoryService
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月24日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
public interface CategoryService extends IService<Category> {
  boolean customRemoveById(Long id);
}

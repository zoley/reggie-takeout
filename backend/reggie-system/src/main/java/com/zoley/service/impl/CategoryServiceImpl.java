package com.zoley.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoley.entity.Category;
import com.zoley.mapper.CategoryMapper;
import com.zoley.service.CategoryService;
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
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}

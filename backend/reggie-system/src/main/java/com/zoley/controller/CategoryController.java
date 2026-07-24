package com.zoley.controller;

import com.zoley.common.result.Result;
import com.zoley.entity.Category;
import com.zoley.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.zoley.common.constant.Constant.BASE_PATH;

/**
 * 类 名: CategoryController
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年07月24日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Slf4j
@RequiredArgsConstructor // 有了这个注解就不用手动添加构造方法了，会自动添加一个有所有final字段的构造方法
@RestController
@RequestMapping( BASE_PATH+"/category")
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping("/listByPage")
  public Result<List<Category>> listByPage() {
    return Result.success(categoryService.list());
  }
}


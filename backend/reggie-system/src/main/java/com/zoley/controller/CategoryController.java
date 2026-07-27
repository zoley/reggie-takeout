package com.zoley.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zoley.common.result.Result;
import com.zoley.entity.Category;
import com.zoley.entity.CategorySearch;
import com.zoley.entity.Employee;
import com.zoley.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
  public Result<Page<Category>> listByPage(@RequestBody CategorySearch categorySearch) {
    String name = categorySearch.getName();
    String type = categorySearch.getType();
    LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
    categoryLambdaQueryWrapper.like(StringUtils.hasText(name), Category::getName, name);
    categoryLambdaQueryWrapper.eq(type != null, Category::getType, type);
    categoryLambdaQueryWrapper.orderByAsc(Category::getSort);
    Page<Category> page = new Page<>(categorySearch.getCurrent(), categorySearch.getPageSize());
    Page<Category> pageResult = categoryService.page(page, categoryLambdaQueryWrapper);
    return Result.success(pageResult);
  }

  @PostMapping("/create")
  public Result<Category> create(@RequestBody Category category) {
    boolean flag = categoryService.save(category);
    if (flag) {
      return Result.success(category);
    }
    return Result.error("创建失败");
  }

  @PutMapping("/update")
  public Result<Category> update(@RequestBody Category category) {
    boolean flag = categoryService.updateById(category);
    if (flag) {
      return Result.success("更新成功", category);
    }
    return Result.error("更新失败");
  }

  @DeleteMapping("/delete/{id}")
  public Result<Category> delete(@PathVariable Long id) {
    boolean flag = categoryService.removeById(id);
    if (flag) {
      return Result.success("删除成功");
    }
    return Result.error("删除失败");
  }
  @PostMapping("/batchDelete")
  public Result<Category> deleteBatch(@RequestBody List<Long> ids) {
    boolean isOk = categoryService.removeByIds(ids);
    if (isOk) {
      return Result.success("删除成功");
    }
    return Result.error("删除失败");
  }

  @GetMapping("/getById")
  public Result<Category> getById(@RequestParam Long id) {
    Category category = categoryService.getById(id);
    if (category == null) {
      return Result.error("分类不存在");
    }
    return Result.success(category);
  }

}
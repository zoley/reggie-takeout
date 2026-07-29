package com.zoley.controller;

import com.zoley.common.result.Result;
import com.zoley.entity.Setmeal;
import com.zoley.service.DishService;
import com.zoley.service.SetmealService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping(BASE_PATH+ "/setmeal")
public class SetmealController {
  private  final SetmealService setmealService;

  @PostMapping("/listByPage")
  public Result<List<Setmeal>> listByPage(@RequestBody Setmeal setmeal){
    List<Setmeal> list = setmealService.list();
    return Result.success(list);
  }
}

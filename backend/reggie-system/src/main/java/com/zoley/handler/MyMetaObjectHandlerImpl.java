package com.zoley.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 类 名: MyMetaObjectHandler
 * 描 述:
 * 作 者: 主成睿
 * 创 建：2026年06月15日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
 @Component
 public class MyMetaObjectHandlerImpl implements MetaObjectHandler {

   @Override
   public void insertFill(MetaObject metaObject) {
     this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
     this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
     this.strictInsertFill(metaObject, "createUser", Long.class, 1L);   // 临时填充为1，实际应从登录用户获取
     this.strictInsertFill(metaObject, "updateUser", Long.class, 1L);
   }

   @Override
   public void updateFill(MetaObject metaObject) {
     this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
     this.strictUpdateFill(metaObject, "updateUser", Long.class, 1L);
   }
 }

package com.zoley.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类 名: FileMeta
 * 描 述: 文件元数据。存储名用 UUID，原始名单独入库，下载时按 id 回显原始名。
 * 作 者: 主成睿
 * 创 建：2026年08月02日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@EqualsAndHashCode(callSuper = true)
@TableName("file_meta")
@Data
public class FileMeta extends Base {

  /** 存储文件名(UUID+后缀)，落盘文件名 */
  private String storageName;

  /** 上传时的原始文件名，下载时回显 */
  private String originalName;

  /** MIME 类型，如 image/jpeg */
  private String contentType;

  /** 文件大小(字节) */
  private Long size;

  /** 文件后缀(含.)，如 .jpg */
  private String suffix;

  /** 逻辑删除:0未删,1已删 */
  @TableLogic
  private Integer deleted;

}

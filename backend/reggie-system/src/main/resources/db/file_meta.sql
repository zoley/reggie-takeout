-- 文件元数据表：存储名用 UUID，原始名单独入库，下载时按 id 回显原始名
CREATE TABLE IF NOT EXISTS `file_meta` (
  `id`            BIGINT       NOT NULL                 COMMENT '主键ID(雪花)',
  `storage_name`  VARCHAR(64)  NOT NULL                 COMMENT '存储文件名(UUID+后缀)',
  `original_name` VARCHAR(255) NOT NULL                 COMMENT '上传时的原始文件名',
  `content_type`  VARCHAR(128)                          COMMENT 'MIME类型',
  `size`          BIGINT                                COMMENT '文件大小(字节)',
  `suffix`        VARCHAR(20)                           COMMENT '文件后缀(含.)',
  `create_time`   DATETIME                              COMMENT '创建时间',
  `update_time`   DATETIME                              COMMENT '更新时间',
  `create_user`   BIGINT                                COMMENT '创建人ID',
  `update_user`   BIGINT                                COMMENT '修改人ID',
  `deleted`       TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除:0未删,1已删',
  PRIMARY KEY (`id`),
  KEY `idx_storage_name` (`storage_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件元数据表';

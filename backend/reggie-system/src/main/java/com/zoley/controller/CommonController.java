package com.zoley.controller;

import com.zoley.common.result.Result;
import com.zoley.common.result.ResultCode;
import com.zoley.entity.FileMeta;
import com.zoley.service.FileMetaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.zoley.common.constant.Constant.BASE_PATH;

/**
 * 类 名: CommonController
 * 描 述: 文件上传/下载。存储名用 UUID，原始名入库，下载可按 id 回显原始名。
 * 作 者: 主成睿
 * 创 建：2026年07月31日
 * 版 本：v1.0.0
 * <p>
 * 历 史: (版本) 作者 时间 注释
 */
@Slf4j
@RestController
@RequestMapping(BASE_PATH+"/common")
public class CommonController {
  @Value("${upload.path}")
  private String uploadPath;
  private final FileMetaService fileMetaService;

  public CommonController(FileMetaService fileMetaService) {
    this.fileMetaService = fileMetaService;
  }

  @PostMapping("/file/upload")
  public Result<Map<String,Object>> normalUpload(@RequestParam("file") MultipartFile file) {
    if(file.isEmpty()){
      return Result.error(ResultCode.CODE_422,"请选择图片");
    }
    String contentType = file.getContentType();
    if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
      return Result.error(ResultCode.CODE_422,"请选择图片类型文件");
    }
    File dir = new File(uploadPath);
    if(!dir.exists()){
      boolean created = dir.mkdirs();
      if(!created){
        return Result.error(ResultCode.CODE_435,"无法创建上传目录，请检查权限");
      }
    }
    String originalFileName = file.getOriginalFilename();
    String suffix = "";
    if (originalFileName != null && originalFileName.contains(".")) {
      suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
    }
    // 存储名用 UUID：避免冲突、规避中文/特殊字符落地问题
    String storageName = UUID.randomUUID().toString().replace("-","") + suffix;
    String lastPath = uploadPath.endsWith(File.separator) ? uploadPath + storageName : uploadPath + File.separator + storageName;
    try {
      file.transferTo(new File(lastPath));
    } catch (IOException e) {
      log.error("文件上传失败",e);
      return Result.error(ResultCode.CODE_435, "文件保存失败：" + e.getMessage());
    }
    // 元数据入库，下载时按 id 回显原始名
    FileMeta meta = new FileMeta();
    meta.setStorageName(storageName);
    meta.setOriginalName(originalFileName == null ? storageName : originalFileName);
    meta.setContentType(contentType);
    meta.setSize(file.getSize());
    meta.setSuffix(suffix);
    fileMetaService.save(meta);

    Map<String, Object> resultData = new HashMap<>();
    // 雪花 ID 转 String，避免前端 JS 大数精度丢失
    resultData.put("id", String.valueOf(meta.getId()));
    resultData.put("path", storageName);
    resultData.put("originalName", meta.getOriginalName());
    return Result.success(resultData);
  }

  /**
   * 按 id 下载（推荐）：查库取存储名与原始名，浏览器另存为显示原始名
   */
  @GetMapping("/file/download/{id}")
  public ResponseEntity<Resource> downloadById(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean isAttachment){
    FileMeta meta = fileMetaService.getById(id);
    if(meta == null){
      log.error("文件元数据不存在:id={}",id);
      return ResponseEntity.notFound().build();
    }
    return buildDownloadResponse(meta.getStorageName(), meta.getOriginalName(), isAttachment);
  }

  /**
   * 旧版按存储名下载：兼容业务表里已存的 path 字段
   */
  @GetMapping("/file/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam String fileName,@RequestParam(defaultValue = "false") boolean isAttachment){
    return buildDownloadResponse(fileName, fileName, isAttachment);
  }

  /**
   * 构造下载响应：storageName 定位磁盘文件，displayFileName 用于浏览器另存为
   */
  private ResponseEntity<Resource> buildDownloadResponse(String storageName, String displayFileName, boolean isAttachment){
    Path basePath = Paths.get(uploadPath).normalize();
    Path realPath = basePath.resolve(storageName).normalize();
    if(!realPath.startsWith(basePath)){
      log.error("路径有问题:{}",realPath);
      return ResponseEntity.badRequest().build();
    }
    Resource resource;
    try {
      resource = new UrlResource(realPath.toUri());
    } catch (MalformedURLException e) {
      log.error("资源有问题");
      return ResponseEntity.badRequest().build();
    }
    if(!resource.exists() || !resource.isReadable()){
      log.error("资源不存在:{}",realPath);
      return ResponseEntity.notFound().build();
    }
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(probeContentType(realPath));
    // 兼容中文文件名：filename 给老浏览器兜底，filename* 给现代浏览器
    String encoded = URLEncoder.encode(displayFileName, StandardCharsets.UTF_8).replace("+", "%20");
    String disposition = (isAttachment?"attachment":"inline")
        + ";filename=\"" + encoded + "\""
        + ";filename*=UTF-8''" + encoded;
    httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, disposition);
    return ResponseEntity.ok().headers(httpHeaders).body(resource);
  }

  private MediaType probeContentType(Path path){
    try {
      String mine = Files.probeContentType(path);
      if(mine!=null && !mine.isEmpty()){
        return MediaType.parseMediaType(mine);
      }
    } catch (IOException ignored) {
    }
    return MediaType.APPLICATION_OCTET_STREAM;
  }

}

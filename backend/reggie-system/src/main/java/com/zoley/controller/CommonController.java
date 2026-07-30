package com.zoley.controller;

import com.zoley.common.result.Result;
import com.zoley.common.result.ResultCode;
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
 * 描 述:
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

  @PostMapping("/file/upload")
  public Result<Map<String,String>> normalUpload(@RequestParam("file") MultipartFile file) {
    System.out.println(file.getOriginalFilename());
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
    String suffix="";
    if (originalFileName != null) {
       suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
    }
    String newFileName = UUID.randomUUID().toString().replace("-","") + suffix;
    String lastPath = uploadPath.endsWith(File.separator) ? uploadPath + newFileName : uploadPath + File.separator + newFileName;
    try {
      file.transferTo(new File(lastPath));
    } catch (IOException e) {
      log.error("文件上传失败",e);
      return Result.error(ResultCode.CODE_435, "文件保存失败：" + e.getMessage());
    }
    Map<String, String> resultData = new HashMap<>();
    resultData.put("path",newFileName);
    return Result.success(resultData);
  }

  @GetMapping("/file/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam String fileName,@RequestParam(defaultValue = "false") boolean isAttachment){
    Path basePath = Paths.get(uploadPath).normalize();
    Path realPath = basePath.resolve(fileName).normalize();
    if(!realPath.startsWith(basePath)){
      log.error("路径有问题:{}",realPath);
      return ResponseEntity.badRequest().build();
    }
    // 加载资源
    Resource resource = null;
    try {
      resource = new UrlResource(realPath.toUri());
    } catch (MalformedURLException e) {
      log.error("资源有问题");
      return ResponseEntity.badRequest().build();
    }
    if(!resource.exists() || !resource.isReadable()){
      log.error("资源不存在？");
      return ResponseEntity.badRequest().build();
    }
    HttpHeaders httpHeaders = new HttpHeaders();
    MediaType mediaType = probeContentType(realPath);
    httpHeaders.setContentType(mediaType);
    //解决中文乱码
    String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("\\+", "%20");
    String disposition = (isAttachment?"attachment":"inline")+";filename=\""+encodedFileName+"\"";
    httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION,disposition);
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

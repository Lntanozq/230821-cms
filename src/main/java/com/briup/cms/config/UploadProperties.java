package com.briup.cms.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@Component
public class UploadProperties {

	/**
	 * nginx 服务器路径
	 */
	@Value("${upload.nginx.nginxPath}")
	private String nginxPath;

	/**
	 * 本项目图文资源在nginx中存放的文件夹路径
	 */
	@Value("${upload.nginx.basePath}")
	private String basePath;

	/**
	 * OSS Access key
	 */
	@Value("${upload.OSS.accessKey}")
	private String accessKey;

	/**
	 * OSS Secret key
	 */
	@Value("${upload.OSS.secretKey}")
	private String secretKey;

	/**
	 * bucketName
	 */
	@Value("${upload.OSS.bucket}")
	private String bucket;

	/**
	 * url地址,用于拼接 文件上传成功后回显的url
	 */
	@Value("${upload.OSS.baseUrl}")
	private String baseUrl;
}

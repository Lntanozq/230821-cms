package com.briup.cms.util;

import com.briup.cms.config.UploadProperties;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Component
public class UploadUtils {

	@Resource
	private UploadProperties uploadProperties;

	/**
	 * @param file 待上传的文件
	 * @return 上传成功后回显的url路径
	 */
	public String fileToLocal(MultipartFile file)
			throws IOException {

		log.info("文件上传到本地nginx中:{}", file.getOriginalFilename());

		//指定文件上传路径(本地nginx服务其中)
		String parentPath = uploadProperties.getNginxPath()
				+ uploadProperties.getBasePath() + "/";

		//获取需要上传的文件名称
		String filename = generateFilePath(file);

		log.info("上传到服务器后的文件名称:{}", filename);


		//创建输出文件对象
		File file2 = new File(parentPath, filename);
		//如果父目录不存在,先创建父目录
		File directory = file2.getParentFile();
		if (!directory.exists()) {
			boolean mkdirs = directory.mkdirs();
		}

		//文件上传本地
		file.transferTo(file2);

		//文件上传成功后,给返回的路径赋值,以存入数据库中,方便在浏览器中访问
		//注意nginx端口号,默认的 80 端口可以省略
		String url = "http://localhost/" + uploadProperties.getBasePath()
				+ "/" + filename;

		log.info("文件上传成功,文件地址:{}", url);

		return url;
	}

	/**
	 * @param file 待上传的文件
	 * @return 上传成功后回显的url路径
	 */
	public String fileToOSS(MultipartFile file) throws Exception {
		log.info("文件上传到七牛云OSS:{}", file.getOriginalFilename());

		//构造一个带指定 Region 对象的配置类(根据七牛云服务器地区进行设置,
		//这里autoRegion会自动匹配相应地区七牛云服务)
		Configuration configuration = new Configuration(Region.autoRegion());

		//将配置传入UploadManager
		UploadManager uploadManager = new UploadManager(configuration);

		//验证AK与SK,AK与SK从配置类中获取
		Auth auth = Auth.create(uploadProperties.getAccessKey(),
				uploadProperties.getSecretKey());

		//指定桶 从配置类中获取
		String upToken = auth.uploadToken(uploadProperties.getBucket());

		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String fileName = generateFilePath(file);

		//上传文件
		Response response = uploadManager.put(file.getInputStream(), fileName,
				upToken, null, null);

		//解析上传成功的结果
		DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),
				DefaultPutRet.class);

		log.info("文件上传成功,文件地址:{}", uploadProperties.getBaseUrl() + fileName);

		return uploadProperties.getBaseUrl() + fileName;
	}


	/**
	 * 随机生成文件在服务器上的名字, 使用UUID作为名称保证文件的唯一性(同时避免文件名乱码)
	 *
	 * @param file 源文件,用于获取文件 原名
	 * @return 文件的新名
	 */
	private String generateFilePath(MultipartFile file) {
		//通过上传日期,对文件进行分类
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/");
		String datePate = format.format(new Date());

		//获取文件名称
		String filename = file.getOriginalFilename();

		//给文件做唯一标识
		assert filename != null;
		return datePate + UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
	}

}
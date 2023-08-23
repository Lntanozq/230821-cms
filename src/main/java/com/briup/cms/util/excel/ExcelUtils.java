package com.briup.cms.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author horry
 * @Description 使用EasyExcel操作excel导入导出数据的工具类
 * @date 2023/8/22-10:39
 */
@Component
public class ExcelUtils {

	/**
	 * 无需注册自定义类型转换时导入使用
	 */
	@SneakyThrows
	public <T> List<T> importData(MultipartFile file, Class<T> tClass,
	                              AnalysisEventListener<T> listener) {
		return importData(file, tClass, listener, (Converter<?>) null);
	}

	/**
	 * 从Excel文件中导入数据
	 *
	 * @param file       数据文件对象
	 * @param tClass     待导入数据的类型
	 * @param listener   自定义监听器
	 * @param converters 自定义参数转换器列表
	 * @param <T>        泛型
	 * @return 导入的数据
	 */
	@SneakyThrows
	public <T> List<T> importData(MultipartFile file, Class<T> tClass,
	                              AnalysisEventListener<T> listener, Converter<?>... converters) {
		//获取文件的输入流
		InputStream in = file.getInputStream();

		//调用read方法
		ExcelReaderBuilder readerBuilder = EasyExcel.read(in)
				//注册自定义监听器，字段校验可以在监听器内实现
				.registerReadListener(listener);

		//注册自定义参数转换器
		if (converters != null) {
			for (Converter<?> converter : converters) {
				readerBuilder.registerConverter(converter);
			}
		}

		//对应导入的实体类
		List<T> list = readerBuilder.head(tClass)
				//导入数据的sheet页编号，0代表第一个sheet页，如果不填，则会导入所有sheet页的数据
				.sheet()
				//列表头行数，1代表列表头有1行，第二行开始为数据行
				.headRowNumber(1)
				//开始读Excel，返回一个List<T>集合，继续后续入库操作
				.doReadSync();

		//关闭资源
		in.close();

		return list;
	}

	/**
	 * 无需注册自定义类型转换时导出使用
	 */
	@SneakyThrows
	public <T> void exportExcel(HttpServletResponse response, List<T> list, Class<T> tClass,
	                            String fileName) {
		exportExcel(response, list, tClass, fileName, (Converter<?>[]) null);
	}

	/**
	 * 导出数据到excel,单sheet表
	 *
	 * @param response   响应对象
	 * @param list       待导出的数据
	 * @param tClass     导出数据的类型
	 * @param fileName   文件名称
	 * @param converters 自定义参数转换器列表
	 */
	@SneakyThrows
	public <T> void exportExcel(HttpServletResponse response, List<T> list, Class<T> tClass,
	                            String fileName, Converter<?>... converters) {
		//获取输出流
		OutputStream outputStream = response.getOutputStream();
		//设置响应结果
		this.setExcelResponseProp(response, fileName);

		//获取导出对象
		ExcelWriter excelWriter = getExcelWriter(outputStream, tClass, converters);

		//创建一个sheet表
		WriteSheet sheet = EasyExcel.writerSheet().build();
		//导出数据
		excelWriter.write(list, sheet);

		//导出完毕后刷新等收尾工作
		excelWriter.finish();
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 无需自定义类型转换时导出多sheet表使用
	 */
	@SneakyThrows
	public <T> void exportManySheet(HttpServletResponse response, List<T> list, Class<T> tClass,
	                                String fileName) {
		exportManySheet(response, list, tClass, fileName, (Converter<?>[]) null);
	}

	/**
	 * 导出数据到excel,多sheet表
	 *
	 * @param response  响应对象
	 * @param list      待导出的数据
	 * @param tClass    导出数据的类型
	 * @param fileName  文件名称
	 * @param converter 自定义参数转换器
	 */
	@SneakyThrows
	public <T> void exportManySheet(HttpServletResponse response, List<T> list, Class<T> tClass,
	                                String fileName, Converter<?>... converter) {
		//获取输出流
		OutputStream outputStream = response.getOutputStream();
		//设置响应结果
		this.setExcelResponseProp(response, fileName);

		//获取导出对象
		ExcelWriter excelWriter = getExcelWriter(outputStream, tClass, converter);

		//将需要导出的数据进行分割,每1000条数据放入到一个sheet中
		int size = list.size();
		int total = 1;
		for (int i = 0; i < size; i += 1000) {
			//创建sheet对象,命名方式 如:日志信息1、日志信息2....
			WriteSheet writeSheet = EasyExcel.writerSheet(fileName + total++).build();
			//将数据写出到sheet中
			excelWriter.write(list.subList(i, size >= 1000 ? i + 1000 : size), writeSheet);
		}

		//导出完毕后刷新等收尾工作
		excelWriter.finish();
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 根据已有条件,创建导出对象
	 */
	private <T> ExcelWriter getExcelWriter(OutputStream outputStream, Class<T> tClass,
	                                       Converter<?>... converters) {
		//获取导出对象建造者
		ExcelWriterBuilder writerBuilder = EasyExcel.write(outputStream, tClass)
				.excelType(ExcelTypeEnum.XLSX);
		//注册自定义类型转换器
		if (converters != null) {
			for (Converter<?> converter : converters) {
				writerBuilder.registerConverter(converter);
			}
		}
		return writerBuilder.build();
	}

	/**
	 * 设置响应结果
	 *
	 * @param response    响应结果对象
	 * @param rawFileName 文件名
	 * @throws UnsupportedEncodingException 不支持的编码异常
	 */
	private void setExcelResponseProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
		//设置内容类型
		response.setContentType("application/octet-stream");
		//设置编码格式
		response.setCharacterEncoding("utf-8");
		//设置导出文件名称（避免乱码）
		String fileName = URLEncoder.encode(rawFileName.concat(".xlsx"), "UTF-8");
		// 设置响应头
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
	}
}

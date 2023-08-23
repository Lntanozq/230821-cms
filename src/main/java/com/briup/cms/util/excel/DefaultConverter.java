package com.briup.cms.util.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author horry
 * @Description 对Integer类型 默认的参数转换处理器
 * @date 2023/8/22-19:03
 */
public class DefaultConverter implements Converter<Integer> {
	@Override
	public Class<?> supportJavaTypeKey() {
		return Integer.class;
	}

	@Override
	public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration){
		return Integer.parseInt(cellData.getStringValue());
	}

	@Override
	public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration){
		return new WriteCellData<Integer>(String.valueOf(value));
	}
}

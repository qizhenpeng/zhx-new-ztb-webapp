package com.techvalley.ztb.common;

import com.techvalley.search.hbase.core.AbstractPage;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.FilterList;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PageDemo extends AbstractPage<String> {
//	public static final String TABLE_NAME="record";
//	protected static final String ColumnFamily="data";
//	private static final String columnInfo="info";
	
	public PageDemo(){
		super();
	}
	
	@Override
	protected List<Get> getList(List<byte[]> rowList) {
		List<Get> list = new LinkedList<Get>();
		for (byte[] row : rowList) {
			Get get = new Get(row);
			get.addFamily(Constants.COLUMN_FAMILY.getBytes());
			list.add(get);
		}
		return list;
	}

	@Override
	protected Map<byte[], byte[]> packFamilyMap(Result result) {
		Map<byte[], byte[]> dataMap = null;
		dataMap = new LinkedHashMap<byte[], byte[]>();
		dataMap.putAll(result.getFamilyMap((Constants.COLUMN_FAMILY).getBytes()));
		return dataMap;
	}

	@Override
	protected FilterList packageFilters(boolean isPage) {
		FilterList filterList = null;
		filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		return filterList;
	}

}

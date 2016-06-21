package com.techvalley.parse.common;

import org.apache.hadoop.conf.Configuration;

public class ParserServerConfig {
	public static Configuration HDFS_CONF;
	public static String HBASE_TABLE_NAME;
	public static String HBASE_FAMILY;
	public static int ABSTRACT_SUM;
	public static String HDFS_BASE_PATH;
	static{
		
		//HDFS configuration ;
		ParserServerConfig.HDFS_CONF = new Configuration();
		ParserServerConfig.HDFS_CONF.set("fs.defaultFS", "hdfs://nameservice");
	    HBASE_TABLE_NAME= "ztb_info";
	    HBASE_FAMILY="m";
	    ABSTRACT_SUM = 10;
	    HDFS_BASE_PATH = "/zbt/file";
	}
}

package com.techvalley.ztb.service;

import com.techvalley.search.hbase.core.HBaseHelper;
import com.techvalley.search.hbase.utils.PageTable;
import com.techvalley.ztb.common.PageDemo;
import com.techvalley.ztb.common.Parms;
import com.techvalley.ztb.request.TikaSolrParams;
import com.techvalley.ztb.request.ZbRequest;
import com.techvalley.ztb.util.ColumnFamilyUtils;
import com.techvalley.ztb.util.HTTPClientUtil;
import com.techvalley.ztb.util.JsonMapper;
import com.techvalley.ztb.util.ObjectUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SampleService
{
  private String host = Parms.getUrl();
  private HBaseHelper hbase = new HBaseHelper();
  private PageDemo pd = new PageDemo();
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

  public boolean zbInfoPost(ZbRequest request) {
    String tn = this.sdf.format(new Date());
    String uuid = StringUtils.isEmpty(request.getUuid()) ? UUID.randomUUID().toString() : request.getUuid();
    System.out.println("uuid=====" + uuid);
    List list = ColumnFamilyUtils.getColumnFamilies(request);
    try {
      this.hbase.insert("ztb_info", uuid, list);
      TikaSolrParams tikaSolrParams = new TikaSolrParams();
      tikaSolrParams.setTn(uuid);
      tikaSolrParams.setRowKey(uuid);
      tikaSolrParams.setUserId(uuid);
      tikaSolrParams.setPath(request.getFilePath());
      Map keys = ObjectUtils.getFiledsValueMap(request);
      keys.remove("filePath");
      keys.remove("uuid");
      tikaSolrParams.setKeys(keys);
      String params = JsonMapper.getDefault().toJson(tikaSolrParams);
      String result = HTTPClientUtil.stringEntityPost(host+"/tika/post", params, null);

      System.out.println("result=============" + result);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public PageTable getZtbs(int start, int end, int current, int size) {
    try {
      return this.pd.page("ztb_info", "" + start, "" + end, Integer.valueOf(current), Integer.valueOf(size));
    }
    catch (IOException e) {
      e.printStackTrace();
    }return null;
  }

  public String getZtbs(String content, int current, int size)
  {
    String result = null;
    try {
      if (null == content)
        result = HTTPClientUtil.httpGet(host+"/tika/query?key=&pageSize=" + size + "&curPage=" + current);
      else
        result = HTTPClientUtil.httpGet(host+"/tika/query?key=" + content + "&pageSize=" + size + "&curPage=" + current);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public String getZtbByUUID(String uuid) {
    try {
      return HTTPClientUtil.httpGet(host+"/tika/hbase/query?tableName=ztb_info&uuid=" + uuid);
    }
    catch (Exception e)
    {
    }

    return null;
  }

  public String delZtbByUUID(String uuid) {
    try {
      return HTTPClientUtil.httpGet(host+"/tika/delete?uuid=" + uuid);
    }
    catch (IOException e) {
      e.printStackTrace();
    }return null;
  }

  public String getResult(String uuid)
  {
    String result = null;
    try {
      result = HTTPClientUtil.httpGet(host+"/tika/get/" + uuid);
    } catch (IOException e) {
      result = "{status:\"500\"}";
      e.printStackTrace();
    }
    return result;
  }
}
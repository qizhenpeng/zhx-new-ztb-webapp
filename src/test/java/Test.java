import com.techvalley.ztb.request.UserInfo;
import com.techvalley.ztb.util.HTTPClientUtil;
import com.techvalley.ztb.util.JsonMapper;

import java.io.IOException;

/**
 * Created by Administrator on 2015/2/2.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        UserInfo request = new UserInfo();
        request.setName("admin");
        Page page = new Page();
        page.setPageSize(3);
        String params =  JsonMapper.getDefault().toJson(page);
        System.out.println(params);
        String result =  HTTPClientUtil.stringEntityPost("http://localhost:8080/graph/templateDetailRelation/get", params, null);
        System.out.println(result);
        result =  HTTPClientUtil.httpGet("http://localhost:8080/graph/templateDetailRelation/get/1");
        System.out.println(result);
    }
}

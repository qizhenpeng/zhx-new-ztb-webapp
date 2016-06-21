package com.techvalley.ztb.controller;

import com.techvalley.ztb.model.Department;
import com.techvalley.ztb.request.UserInfo;
import com.techvalley.ztb.request.ZbRequest;
import com.techvalley.ztb.service.SampleService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/" })
public class SampleController {

	@Autowired
	private SampleService service;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = { "login" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> login(UserInfo request) {
		Map resultMap = new HashMap();
		if (("admin".equalsIgnoreCase(request.getName()))
				&& ("admin".equalsIgnoreCase(request.getPassword()))) {
			resultMap.put("status", "200");
			resultMap.put("msg", "success");
		} else {
			resultMap.put("status", "500");
			resultMap.put("msg", "failed");
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = { "department/get" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> departments() {
		Map resultMap = new HashMap();
		resultMap.put("status", "200");
		resultMap.put("data", Department.values());
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = { "ztb/post" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> postZtb(ZbRequest request) {
		Map resultMap = new HashMap();
		boolean isSuccess = service.zbInfoPost(request);
		if (isSuccess) {
			resultMap.put("status", "200");
			resultMap.put("msg", "success");
		} else {
			resultMap.put("status", "500");
			resultMap.put("msg", "failed");
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = { "ztb/get/{uuid}" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> getZtbByUUID(@PathVariable String uuid) {
		Map resultMap = new HashMap();

		String data = service.getZtbByUUID(uuid);
		resultMap.put("status", "200");
		resultMap.put("msg", "success");
		resultMap.put("data", data);
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = { "ztb/get" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> getZtb() {
		Map resultMap = new HashMap();
		String result = service.getZtbs(null, 1, 10);
		resultMap.put("status", "200");
		resultMap.put("msg", "success");
		resultMap.put("data", result);

		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = { "ztb/delete" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> delZtbByUUID(String uuid) {
		Map resultMap = new HashMap();

		String result = service.delZtbByUUID(uuid);
		if (null != result) {
			resultMap.put("status", "200");
			resultMap.put("msg", "success");
		} else {
			resultMap.put("status", "500");
			resultMap.put("msg", "failed");
		}
		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = { "ztb/query" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> query(String keyword, int pageIndex, int pageSize) {
		Map resultMap = new HashMap();
		String result = null;
		if ((null == keyword) || (keyword.length() == 0))
			result = service.getZtbs(null, pageIndex, pageSize);
		else {
			result = service.getZtbs(keyword.replace(" ", ""), pageIndex,
					pageSize);
		}
		resultMap.put("status", "200");
		resultMap.put("msg", "success");
		resultMap.put("data", result);
		return resultMap;
	}

	@RequestMapping(value = { "ztb/result/{uuid}" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public String queryResult(@PathVariable String uuid) {
		String result = service.getResult(uuid);
		return result;
	}

}
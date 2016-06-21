package com.techvalley.ztb.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techvalley.ztb.request.UserInfo;

/**
 * Created by Administrator on 2015/2/1.
 */
public class ObjectUtils {
    /**
     * 根据属性名获取属性值
     * */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性名数组
     * */
    public static String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            System.out.println(fields[i].getType());
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
    public static List getFiledsInfo(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        List list = new ArrayList();
        Map infoMap=null;
        for(int i=0;i<fields.length;i++){
            infoMap = new HashMap();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(infoMap);
        }
        return list;
    }
    /**
     * 获取属性及其值的MAP
     * */
    public static Map<String,Object> getFiledsValueMap(Object o){
        Field[] fields= o.getClass().getDeclaredFields();
        Map infoMap = new HashMap();;
        for(int i=0;i<fields.length;i++){
            infoMap.put(fields[i].getName(),getFieldValueByName(fields[i].getName(), o));
        }
        return infoMap;
    }

    /**
     * 获取对象的所有属性值，返回一个对象数组
     * */
    public static Object[] getFiledValues(Object o){
        String[] fieldNames=getFiledName(o);
        Object[] value=new Object[fieldNames.length];
        for(int i=0;i<fieldNames.length;i++){
            value[i]=getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }

    public static void main(String[] args) {
        UserInfo user = new UserInfo();
        user.setName("lindi");
        user.setPassword("123456");
        List list = getFiledsInfo(user);
        for (int i=0; i<list.size();i++) {
            HashMap map = (HashMap) list.get(i);
            System.out.println("type:"+map.get("type"));
            System.out.println("name:"+map.get("name"));
            System.out.println("vaue:"+map.get("value"));
        }
    }
}

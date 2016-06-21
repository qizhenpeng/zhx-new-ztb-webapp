package com.techvalley.ztb.util;

import com.techvalley.search.hbase.utils.ColumnFamily;
import com.techvalley.ztb.common.Constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/1.
 */
public class ColumnFamilyUtils {

    public static ColumnFamily getInstance(String family, String qualifier, Object value){
        ColumnFamily cf = new ColumnFamily();
        cf.setFamily(family);
        cf.setQualifier(qualifier);
        cf.setValue(value);
        return cf;
    }
    public static List<ColumnFamily> getColumnFamilies(Object object){
        List<ColumnFamily> list = new ArrayList<ColumnFamily>();
        Field[] fields=object.getClass().getDeclaredFields();
        for(int i=0;i<fields.length;i++){
            Class clazz = fields[i].getType();
            String name = fields[i].getName();
            Object value = ObjectUtils.getFieldValueByName(name,object);
            ColumnFamily cf = getInstance(Constants.COLUMN_FAMILY, name, value);
            list.add(cf);
        }
        return list;
    }

//    public static void main(String[] args){
//        User user = new User();
//        user.setId(1l);
//        user.setName("lindi");
//        user.setAge(20);
//        List list =  new ColumnFamilyUtils().getColumnFamilies(user);
//        System.out.println(list.size());
//    }
}

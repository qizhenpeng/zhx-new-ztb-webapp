package com.techvalley.ztb.model;

/**
 * Created by lindi
 *  2015/1/29.
 */
public enum Department {
    BM1("01","部门一"),BM2("02","部门二"),BM3("03","部门三"),BM4("04","部门四"),BM5("05","部门五");
    private String code;
    private String name;
    private Department(String code,String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

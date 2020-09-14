package com.efy.enums;

/**
 * @Project xmbb
 * @Date 2020/9/13 19:27
 * @Created by Efy
 * @Description TODO
 */
public enum FileErrorEnum {
    HEAD_DATA_INVALID("0000","文件格式错误!!!"),
    CONTROL_DATA_INVALID("0001","控制器数据错误!!!"),



    ;

    public String code;
    public String desc;

    FileErrorEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

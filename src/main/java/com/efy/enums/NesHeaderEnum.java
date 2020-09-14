package com.efy.enums;

/**
 * @Project xmbb
 * @Date 2020/9/13 18:43
 * @Created by Efy
 * @Description TODO
 */
public enum NesHeaderEnum {
    MIRROR_TYPE_V(1,"垂直"),
    MIRROR_TYPE_H(0,"水平"),

    VS_SYSTEM_N(0,"N"),
    VS_SYSTEM_Y(1,"Y"),

    VIDEO_TYPE_PAL(0,"PAL"),
    VIDEO_TYPE_NTSC(0,"NTSC"),


    ;

    public int code;
    public String desc;

    NesHeaderEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

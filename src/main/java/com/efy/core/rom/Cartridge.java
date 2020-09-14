package com.efy.core.rom;

import java.util.Arrays;

/**
 * @Project xmbb
 * @Date 2020/9/13 21:10
 * @Created by Efy
 * @Description 卡带结构
 */
public class Cartridge {
    public byte[] prg;          //程序内存 16kb每块
    public byte[] chr;          //图形内存 8kb每块
    public int mapper;          //Mapper编号
    public int mirror;          //0-水平 1-垂直
    public boolean fourScreen;  //是否4屏(即是否存在vram)
    public boolean battery;     //是否存在电池

    public Cartridge(NesFileHeader header) {
        this.prg = new byte[header.prgNum * 16 * 1024];
        //补偿图形内存,最少分配8kb
        this.chr = new byte[header.chrNum == 0 ? 1 : header.chrNum * 8 * 1024];
        //ctrl1是低4位,右位移4位即高4位右移并忽略低4位
        //ctrl2是高4位，&运算0xF0即表示直接忽略低4位，
        //之后使用|运算,得到最终值
        this.mapper = (header.ctrl1.origin >> 4) | (header.ctrl2.origin & 0xF0);
        this.mirror = header.ctrl1.mirrorType.code;
        this.fourScreen = header.ctrl1.vRam;
        this.battery = header.ctrl1.batteryBacked;
    }

    @Override
    public String toString() {
        return "Cartridge{" +
                "prg=" + Arrays.toString(prg) +
                ", chr=" + Arrays.toString(chr) +
                ", mapper=" + mapper +
                ", mirror=" + mirror +
                ", fourScreen=" + fourScreen +
                ", battery=" + battery +
                '}';
    }
}

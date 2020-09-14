package com.efy.core.rom;

import com.efy.enums.FileErrorEnum;
import com.efy.enums.NesHeaderEnum;
import com.efy.exception.FileInvalidException;
import com.efy.util.StringUtil;

import java.util.Arrays;

/**
 * @Project xmbb
 * @Date 2020/9/13 18:38
 * @Created by Efy
 * @Description TODO
 */
public class Controller {
    //镜像类型(使用2控制器时,表示卡带是否带有VS-System)
    NesHeaderEnum mirrorType;
    //是否存在电池供电-SRAM,如果存在,则映射内存到$6000-$7FFF(使用2控制器时,必须为0)
    boolean batteryBacked;
    //是否存在Trainer RAM,如果存在,则映射内存到$7000-$71FF(使用2控制器时,必须为0)
    boolean trainer;
    //是否存在VRAM(使用2控制器时,必须为0,为1时忽略mirrorType)
    boolean vRam;
    //MapperType的低4位或高4位值(control1为低4位,control2为高4位)
    int[] mapperType = new int[4];
    //原始byte
    byte origin;
    /**
     * 控制1
     * @param controlByte  N N N N F T B M
     * N: Mapper编号低4位
     * F: 4屏标志位. (如果该位被设置, 则忽略M标志)
     * T: Trainer标志位.  1表示 $7000-$71FF加载 Trainer
     * B: SRAM标志位 $6000-$7FFF拥有电池供电的SRAM.
     * M: 镜像标志位.  0 = 水平, 1 = 垂直.
     * 控制2
     * @param controlByte  N N N N X X P V
     * N: Mapper编号高4位
     * X: 未使用
     * P: Playchoice 10标志位. 被设置则表示为PC-10游戏
     * V: Vs. Unisystem标志位. 被设置则表示为Vs.游戏
     * @param index        控制器序号(1-1号控制器 2-2号控制器)
     */
    public Controller(byte controlByte,int index) throws FileInvalidException {
        this.origin = controlByte;
        String controlBin = Integer.toBinaryString(controlByte);
        //不足8位的补偿到8位
        controlBin = StringUtil.fillToLen(controlBin,8);
        if(checkValid(controlBin,index)){
            decode(controlBin,index);
        }else{
            FileErrorEnum err = FileErrorEnum.CONTROL_DATA_INVALID;
            throw new FileInvalidException(err.code,err.desc);
        }
    }

    private boolean checkValid(String controlBin,int index){
        String bit2To4 = controlBin.substring(1,4);
        //2号控制器的3~7位必须全部为0
        return index != 2 || bit2To4.equals("000");
    }

    private void decode(String controlBin,int index){
        char[] charList = controlBin.toCharArray();
        //二进制位是从右往左数,因此从0~7需要倒叙一下数组
        if(index == 2){
            this.mirrorType = Integer.valueOf(charList[7]) == NesHeaderEnum.VS_SYSTEM_N.code ?
                    NesHeaderEnum.VS_SYSTEM_N : NesHeaderEnum.VS_SYSTEM_Y;
        }else{
            this.mirrorType = Integer.valueOf(charList[7]) == NesHeaderEnum.MIRROR_TYPE_H.code ?
                    NesHeaderEnum.MIRROR_TYPE_H : NesHeaderEnum.MIRROR_TYPE_V;
        }

        this.batteryBacked = Integer.valueOf(charList[6]) == 1;
        this.trainer = Integer.valueOf(charList[5]) == 1;
        this.vRam = Integer.valueOf(charList[4]) == 1;
        mapperType[0] = Integer.valueOf(charList[3]);
        mapperType[1] = Integer.valueOf(charList[2]);
        mapperType[2] = Integer.valueOf(charList[1]);
        mapperType[3] = Integer.valueOf(charList[0]);
    }

    @Override
    public String toString() {
        return "Controller{" +
                "mirrorType=" + mirrorType +
                ", batteryBacked=" + batteryBacked +
                ", trainer=" + trainer +
                ", vRam=" + vRam +
                ", mapperType=" + Arrays.toString(mapperType) +
                '}';
    }
}

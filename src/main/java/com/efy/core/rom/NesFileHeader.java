package com.efy.core.rom;

import com.efy.enums.FileErrorEnum;
import com.efy.enums.NesHeaderEnum;
import com.efy.exception.FileInvalidException;
import com.efy.util.StringUtil;

import java.util.Arrays;

/**
 * @Project xmbb
 * @Date 2020/9/13 18:28
 * @Created by Efy
 * @Description nes文件头
 */
public class NesFileHeader {
    public String magicHead;           //最开始的4个字节必须为"NES^Z"
    public int prgNum;                 //程序块个数(第5字节,PRG-ROM的个数,每块16kb)
    public int chrNum;                 //图形块个数(第6字节,CHR-ROM的个数,每块8kb)
    public Controller ctrl1;           //控制器1(第7字节)
    public Controller ctrl2;           //控制器2(第8字节)
    public int ramNum;                 //内存个数(第9字节,每块8kb)
    public NesHeaderEnum videoType;    //视频类型(第10字节第一个bit位为0-PAL,否则为NTSC)
    public byte[] holdByte;            //保留字段(11-16个字节)

    public NesFileHeader(byte[] head) throws FileInvalidException {
        byte[] mHead = new byte[]{head[0],head[1],head[2],head[3]};
        if(checkValid(mHead)){
            this.magicHead = new String(mHead);
            decode(head);
        }else{
            throw new FileInvalidException(FileErrorEnum.HEAD_DATA_INVALID);
        }
    }

    private void decode(byte[] head) throws FileInvalidException{
        prgNum = (int) head[4];
        chrNum = (int) head[5];
        ctrl1 = new Controller(head[6],1);
        ctrl2 = new Controller(head[7],2);
        ramNum = (int) head[8];
        videoType = StringUtil.byteToString(head[9]).charAt(7) == '0' ?
                    NesHeaderEnum.VIDEO_TYPE_PAL : NesHeaderEnum.VIDEO_TYPE_NTSC;
        holdByte = new byte[]{head[10],head[11],head[12],head[13],head[14],head[15]};
    }

    private boolean checkValid(byte[] mHead){
        byte[] checkArray = new byte[]{0x4E,0x45,0x53,0x1A};
        for(int i=0;i<mHead.length;i++){
            if(checkArray[i] != mHead[i]) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NesFileHeader{" +
                "magicHead='" + magicHead + '\'' +
                ", prgNum=" + prgNum +
                ", chrNum=" + chrNum +
                ", ctrl1=" + ctrl1 +
                ", ctrl2=" + ctrl2 +
                ", ramNum=" + ramNum +
                ", videoType=" + videoType +
                ", holdByte=" + Arrays.toString(holdByte) +
                '}';
    }
}

package com.efy.core;

import com.efy.core.listener.FileLoadListener;
import com.efy.core.listener.impl.NESListener;
import com.efy.core.rom.Cartridge;
import com.efy.core.rom.NesFileHeader;
import com.efy.exception.FileInvalidException;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Project xmbb
 * @Date 2020/9/13 17:59
 * @Created by Efy
 * @Description TODO
 */
public class RomLoader {
    private static FileLoadListener fileLoadListener = new NESListener();

    public static Cartridge read(File file){
        byte[] head = read(file,0,16);
        NesFileHeader fileHeader = null;
        try {
            fileHeader = new NesFileHeader(head);
        } catch (FileInvalidException e){
            fileLoadListener.onLoadError(e);
        }
        return new Cartridge(fileHeader);
    }

    /**
     * @param file    rom文件
     * @param offset  偏移量
     * @param len     读取字节数
     * @return
     */
    public static byte[] read(File file,int offset,int len){
        byte[] readed = new byte[len];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int readLen = fis.read(readed,offset,len);
            if (readLen != len) System.err.println("读取字节数不符");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(fis);
        }
        return readed;
    }

    public static void closeStream(Closeable... closeables){
        for(Closeable closeable : closeables){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setFileLoadListener(FileLoadListener fileLoadListener) {
        RomLoader.fileLoadListener = fileLoadListener;
    }

    public static void main(String[] args) {
        String filePath = "D:\\模拟器\\nes\\绿色兵团 [SW汉化].nes";
        Cartridge rom = RomLoader.read(new File(filePath));
        System.out.println(rom);
    }
}

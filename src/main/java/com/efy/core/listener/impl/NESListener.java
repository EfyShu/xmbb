package com.efy.core.listener.impl;

import com.efy.core.listener.FileLoadListener;

import java.io.File;

/**
 * @Project xmbb
 * @Date 2020/9/13 20:08
 * @Created by Efy
 * @Description TODO
 */
public class NESListener implements FileLoadListener {
    @Override
    public void onLoadError(Throwable t) {
        System.err.println(t);
    }

    @Override
    public void onLoadSucc(File file) {
        System.out.println(file.getName() + "文件加载成功!!");
    }
}

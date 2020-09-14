package com.efy.core.listener;

import java.io.File;

/**
 * @Project xmbb
 * @Date 2020/9/13 19:51
 * @Created by Efy
 * @Description 文件读取监听器
 */
public interface FileLoadListener {

    void onLoadError(Throwable t);

    void onLoadSucc(File file);

}

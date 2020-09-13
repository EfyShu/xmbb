package com.efy.listener;


import javax.swing.*;


/**
 * 按钮监听器接口
 * @author Efy
 *
 */
public interface IButtonListener {

    /**
     * 按钮添加监听器
     */
    void addListener(AbstractButton button, String[] strList);
}

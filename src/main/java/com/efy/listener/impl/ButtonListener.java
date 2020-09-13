package com.efy.listener.impl;


import com.efy.frame.InvokeByThread;
import com.efy.listener.IButtonListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * 按钮监听器 实现类
 * 
 * @author Efy
 * 
 */
public class ButtonListener implements IButtonListener {
	private Map<String, Object> objs = new HashMap<String, Object>();
	
	public void excute(final AbstractButton button, final String[] strList, final Object devObj) {
		int i = 0;
		for (String str : strList) {
			i++;
			if (button.getText().equals(str)) {
				String method = strList[i].replaceAll("[@\\d{1}]*[#\\d{1}]*", "");
				// 如果是打开方法还需监听打开状态
//				if (method.equals("open")) {
//					checkOpen(devObj, button);
//				}
				if (!method.contains(":")) {
					InvokeByThread.invokeMethod(devObj, method, null);
				} else {
					String[] param = new String[method.split(":")[1].split(",").length];
					for (int p = 0; p < param.length; p++) {
						param[p] = method.split(":")[1].split(",")[p];
					}
					InvokeByThread.invokeMethod(devObj, method, param);
				}
				break;
			}
		}
	}

	public void checkOpen(final Object devObj, final AbstractButton button) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				boolean flag = false;
				try {
					Field f = devObj.getClass().getField("isOpen");
					while (i < 1000 && !(flag=f.getBoolean(devObj))) {
						i++;
						Thread.sleep(50);
					}
					if (flag) {
						button.setEnabled(false);
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		});
		t.start();
	}

	@Override
	public void addListener(AbstractButton button, final String[] strList) {
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton tempButton = (AbstractButton) e.getSource();
				try {
					//保存实例
					if(!objs.containsKey(strList[1])){
						Class<?> clazz = Class.forName(strList[1]);
						objs.put(strList[1], clazz.newInstance());
					}
					excute(tempButton, strList, objs.get(strList[1]));
				} catch (Exception ex) {
					System.err.println("Cause By:" + ex.getCause());
					System.err.println("Messages:" + ex.getMessage());
				}
			}
		});
	}
}

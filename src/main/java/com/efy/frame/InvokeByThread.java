package com.efy.frame;

import java.util.Arrays;


/**
 * 用线程处理按钮的响应事件
 * @author Efy
 *
 */
public class InvokeByThread {

    public static void invokeMethod(final Object obj,final String method,final Object[] param){
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    if(null != param){
                        Class<?>[] paramsType = new Class[param.length];
                        Arrays.fill(paramsType, String.class);
                        obj.getClass().getMethod(method, paramsType).invoke(obj,param);
                    }else{
                        obj.getClass().getMethod(method).invoke(obj);
                    }
                } catch (Exception e) {
//					System.err.println("Cause By:" + e.getCause());
//					System.err.println("Messages:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}

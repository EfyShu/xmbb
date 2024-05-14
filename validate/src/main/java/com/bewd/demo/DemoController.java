package com.bewd.demo;

import com.bewd.common.ResultData;

/**
 * @Project xmbb
 * @Date 2023/12/12 10:18
 * @Created by Efy
 * @Description TODO
 */
public class DemoController {
    private DemoService demoService = new DemoService();

    public ResultData test(ValidateParam param){
        return demoService.test(param);
    }
}

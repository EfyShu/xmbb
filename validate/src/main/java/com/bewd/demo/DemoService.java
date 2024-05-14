package com.bewd.demo;

import com.bewd.annotations.CheckParams;
import com.bewd.annotations.Module;
import com.bewd.common.ResultData;

/**
 * @Project xmbb
 * @Date 2023/12/12 10:19
 * @Created by Efy
 * @Description TODO
 */
public class DemoService {



    @Module(value = "测试方法",tags = {"demo","校验模块"})
    @CheckParams
    public ResultData test(ValidateParam param){
        return ResultData.ok();
    }
}

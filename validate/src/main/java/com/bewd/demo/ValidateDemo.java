package com.bewd.demo;

import com.bewd.core.ValidateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2018/12/28 15:03
 * @Created by Efy Shu
 * @Description TODO
 */
public class ValidateDemo {
    //演示注入额外规则类,使用RuleClass标记后,会自动引用该规则类(需spring环境,参考ValidateAspect类)
//    @Autowired
//    @RuleClass
//    private TestRuleClass testRuleClass;

    public static ValidateUtil init(Object... rulsClasses){
        ValidateUtil valid = ValidateUtil.build(rulsClasses);
        return valid;
    }

    public static void main(String[] args) {
        ValidateParam param = new ValidateParam();
        ValidateParamChild child1 = new ValidateParamChild();
        ValidateParamChild child2 = new ValidateParamChild();
        child1.setRoleId("E");
        child2.setRoleId("Ef");
        child1.setRoleName("E");
        child1.setListStr(new ArrayList<>());
        param.getChild().setRoleId("E");
        param.getChild().setRoleName("E");
        param.getChild().setListStr(new ArrayList<>());
        param.getChilds().add(child1);
        param.getChilds().add(child2);
        param.setRoleType(2);
        param.setStatus(2);
        param.setArrayInt(new Integer[]{2,3,4});
        param.setArrayStr(new String[]{"2","2","3"});
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        param.setListStr(list);
        //与注入方式选择一种即可,这里只做main方法演示,不使用spring环境
        ValidateUtil valid = init(new TestRuleClass(),new DemoService());
//        valid.setExcludeRules(new String[]{"isNull"});
//        valid.setRequireParam(new String[]{"ValidateParam.arrayInt"});
//        valid.setCheckWithError(false);
        if (valid == null || valid.valid(param)) {
            System.out.println("参数校验完成");
            return;
        }
        for (String field : valid.getErrorFields()) {
            ValidateUtil.ErrorBean error = valid.getError(field);
            System.out.println("参数校验不通过:" + field + "->" + error.getErrorMsg());
        }
    }
}
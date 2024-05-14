package com.bewd.demo;

import com.bewd.annotations.ValidateRule;
import com.bewd.common.RetCode;
import org.springframework.stereotype.Component;

/**
 * @Project xmbb
 * @Date 2021/1/14 21:24
 * @Created by Efy
 * @Description TODO
 */
@Component
public class TestRuleClass {
    public static final String CHECK_ROLE_TYPE = "checkRoleType";

    @ValidateRule(value = "(额外规则类)【@desc】不正确@length",code = RetCode.FAILED)
    public boolean checkRoleType(Integer roleType){
        return roleType == 3;
    }
}

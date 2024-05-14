package com.bewd.demo;

import com.bewd.annotations.Valid;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

/**
 * @Date 2018/12/28 15:08
 * @Created by Efy Shu
 * @Description TODO
 */
public class ValidateParamChild {
    @Valid(rules = {"isNull","isLength","checkRoleRepeat"},length = 1,msg = {"isLength:不能超过@length位"})
    @ApiModelProperty("角色代码")
    private String roleId;
    @Valid(rules = {"isNull"})
    @ApiModelProperty("角色名")
    private String roleName;
    @Valid(rules = {"isNull","isLength"},length = 2)
    @ApiModelProperty("字符串集合")
    private List<String> listStr;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<String> getListStr() {
        return listStr;
    }

    public void setListStr(List<String> listStr) {
        this.listStr = listStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidateParamChild)) return false;
        ValidateParamChild that = (ValidateParamChild) o;
        return Objects.equals(getRoleId(), that.getRoleId()) &&
                Objects.equals(getRoleName(), that.getRoleName()) &&
                Objects.equals(getListStr(), that.getListStr());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getRoleName(), getListStr());
    }

    @Override
    public String toString() {
        return "ValidateParamChild{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", listStr=" + listStr +
                '}';
    }
}

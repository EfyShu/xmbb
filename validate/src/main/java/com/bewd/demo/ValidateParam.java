package com.bewd.demo;

import com.bewd.annotations.Valid;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Date 2018/12/28 15:08
 * @Created by Efy Shu
 * @Description TODO
 */
public class ValidateParam {
    //演示长度校验
    @Valid(rules = {"isLength"},length = 2)
    @ApiModelProperty("字符串数组")
    private String[] arrayStr;
    @Valid(rules = {"isLength"},length = 2)
    @ApiModelProperty("整数数组")
    private Integer[] arrayInt;
    @Valid(rules = {"isLength"},length = 2,msg = {"isLength:(字段级自定义提示替换)【@desc】集合长度超过限制"})
    @ApiModelProperty("字符串集合")
    private List<String> listStr;
    //演示自定义提示替换
    @Valid(rules = {"isNull"},msg = {"isNull:(字段级自定义提示替换):【@desc】不可为空"})
    @ApiModelProperty("角色名")
    private String roleName;
    //演示额外规则类载入校验
    @Valid(rules = {"isNull",TestRuleClass.CHECK_ROLE_TYPE})
    @ApiModelProperty("角色类型")
    private Integer roleType;
    //演示范围校验
    @Valid(rules = {"isNull","isRange"},range = {"1","2"})
    @ApiModelProperty("角色状态")
    private Integer status;
    //空置校验条件,演示跳过校验
    @ApiModelProperty("角色描述")
    private String des;
    //演示子元素嵌套校验
    @Valid(rules = {"isNull"})
    @ApiModelProperty("子元素")
    private ValidateParamChild child = new ValidateParamChild();
    //演示子集合校验
    @Valid(rules = {"isNull"})
    @ApiModelProperty("子集合")
    private List<ValidateParamChild> childs = new ArrayList<>();

    public String[] getArrayStr() {
        return arrayStr;
    }

    public void setArrayStr(String[] arrayStr) {
        this.arrayStr = arrayStr;
    }

    public Integer[] getArrayInt() {
        return arrayInt;
    }

    public void setArrayInt(Integer[] arrayInt) {
        this.arrayInt = arrayInt;
    }

    public List<String> getListStr() {
        return listStr;
    }

    public void setListStr(List<String> listStr) {
        this.listStr = listStr;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public ValidateParamChild getChild() {
        return child;
    }

    public void setChild(ValidateParamChild child) {
        this.child = child;
    }

    public List<ValidateParamChild> getChilds() {
        return childs;
    }

    public void setChilds(List<ValidateParamChild> childs) {
        this.childs = childs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidateParam)) return false;
        ValidateParam that = (ValidateParam) o;
        return Arrays.equals(getArrayStr(), that.getArrayStr()) &&
                Arrays.equals(getArrayInt(), that.getArrayInt()) &&
                Objects.equals(getListStr(), that.getListStr()) &&
                Objects.equals(getRoleName(), that.getRoleName()) &&
                Objects.equals(getRoleType(), that.getRoleType()) &&
                Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getDes(), that.getDes()) &&
                Objects.equals(getChild(), that.getChild()) &&
                Objects.equals(getChilds(), that.getChilds());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getListStr(), getRoleName(), getRoleType(), getStatus(), getDes(), getChild(), getChilds());
        result = 31 * result + Arrays.hashCode(getArrayStr());
        result = 31 * result + Arrays.hashCode(getArrayInt());
        return result;
    }

    @Override
    public String toString() {
        return "ValidateParam{" +
                "arrayStr=" + Arrays.toString(arrayStr) +
                ", arrayInt=" + Arrays.toString(arrayInt) +
                ", listStr=" + listStr +
                ", roleName='" + roleName + '\'' +
                ", roleType=" + roleType +
                ", status=" + status +
                ", des='" + des + '\'' +
                ", child=" + child +
                ", childs=" + childs +
                '}';
    }
}

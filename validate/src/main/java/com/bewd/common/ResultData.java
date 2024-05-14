package com.bewd.common;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * 接口返回值
 */
public class ResultData<T> {
    @ApiModelProperty("请求状态代码")
    private Integer status = 0;
    @ApiModelProperty("请求状态描述")
    private String message;
    @ApiModelProperty("请求返回数据")
    private T data;
    @ApiModelProperty("请求耗时")
    private long cost;


    public static ResultData ok() {
        return ret(RetCode.OK.code,RetCode.OK.msg,0,null);
    }

    public static <T> ResultData<T> ok(T data) {
        return ret(RetCode.OK.code,RetCode.OK.msg,0,data);
    }

    public static <T> ResultData<T> ok(T data,long cost) {
        return ret(RetCode.OK.code,RetCode.OK.msg,cost,data);
    }

    public static ResultData failed(RetCode ret){
        return ret(ret.code,ret.msg,0,null);
    }

    public static ResultData failed(RetCode ret,String msg){
        return ret(ret.code,msg,0,null);
    }

    public static <T> ResultData<T>  failed(RetCode ret,T data){
        return ret(ret.code,ret.msg,0,data);
    }

    public static <T> ResultData<T>  failed(RetCode ret,String msg,T data){
        return ret(ret.code,msg,0,data);
    }

    public static <T> ResultData<T>  failed(RetCode ret,long cost,T data){
        return ret(ret.code,ret.msg,cost,data);
    }

    public static <T> ResultData<T>  failed(RetCode ret,String msg,long cost,T data){
        return ret(ret.code,msg,cost,data);
    }

    public static ResultData failed(int code, String msg) {
        return ret(code,msg,0,null);
    }

    public static <T> ResultData<T> failed(int code, String msg, T data) {
        return ret(code,msg,0,data);
    }

    public static <T> ResultData<T> failed(int code, String msg,long cost, T data) {
        return ret(code,msg,cost,data);
    }

    public static <T> ResultData ret(int code,String msg,long cost,T data){
        ResultData resultData = new ResultData();
        resultData.setStatus(code);
        resultData.setMessage(msg);
        resultData.setCost(cost);
        resultData.setData(data);
        return resultData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String toJSON(){
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("status",status);
        jsonResult.put("message",message);
        jsonResult.put("data",null);
        jsonResult.put("cost",cost);
        return jsonResult.toJSONString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultData)) return false;
        ResultData<?> that = (ResultData<?>) o;
        return getCost() == that.getCost() &&
                Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(getData(), that.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getMessage(), getData(), getCost());
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", cost=" + cost +
                '}';
    }
}

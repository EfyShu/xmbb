package com.bewd.common;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Created by Efy on 2017/12/28.
 */
public class PagingParam {
    @ApiModelProperty("页码,从1开始")
    private Integer page = 1;
    @ApiModelProperty(value="请求行数（默认10）")
    private Integer rows = 10;
    @ApiModelProperty("排序的字段")
    private String sidx;
    @ApiModelProperty("排序的方式")
    private String sord;

    public void setPage(Integer page) {
        if (page == null || page < 1) {
            page = 1;
        }
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PagingParam)) return false;
        PagingParam that = (PagingParam) o;
        return Objects.equals(getPage(), that.getPage()) &&
                Objects.equals(getRows(), that.getRows()) &&
                Objects.equals(getSidx(), that.getSidx()) &&
                Objects.equals(getSord(), that.getSord());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPage(), getRows(), getSidx(), getSord());
    }

    @Override
    public String toString() {
        return "PagingParam{" +
                "page=" + page +
                ", rows=" + rows +
                ", sidx='" + sidx + '\'' +
                ", sord='" + sord + '\'' +
                '}';
    }
}

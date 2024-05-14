package com.bewd.common;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

/**
 * 翻页返回结果
 * Created by Efy on 2017/12/28.
 */
public class PagingDTO<T> {
    @ApiModelProperty("数据列表")
    private List<T> rows;
    @ApiModelProperty("当前页码")
    private Integer page=1;
    @ApiModelProperty("总页数")
    private Integer total=1;
    @ApiModelProperty("总记录数")
    private long records=0L;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PagingDTO)) return false;
        PagingDTO<?> pagingDTO = (PagingDTO<?>) o;
        return getRecords() == pagingDTO.getRecords() &&
                Objects.equals(getRows(), pagingDTO.getRows()) &&
                Objects.equals(getPage(), pagingDTO.getPage()) &&
                Objects.equals(getTotal(), pagingDTO.getTotal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRows(), getPage(), getTotal(), getRecords());
    }

    @Override
    public String toString() {
        return "PagingDTO{" +
                "rows=" + rows +
                ", page=" + page +
                ", total=" + total +
                ", records=" + records +
                '}';
    }
}

package com.example.chatapp.Dtos;

import java.util.List;

public class PagedResultDto<TDto> {
    private Pagination pagination;
    private List<TDto> data;

    public PagedResultDto(Pagination pagination, List<TDto> data) {
        this.pagination = pagination;
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<TDto> getData() {
        return data;
    }

    public void setData(List<TDto> data) {
        this.data = data;
    }
}

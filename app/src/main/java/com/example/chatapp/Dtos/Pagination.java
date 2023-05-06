package com.example.chatapp.Dtos;

public class Pagination {
    private long total;

    private long skip;
    private int limit;

    public Pagination(long total, long skip, int limit) {
        this.total = total;
        this.skip = skip;
        this.limit = limit;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSkip() {
        return skip;
    }

    public void setSkip(long skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
